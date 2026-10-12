package com.poke.music.data.repository

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.poke.music.data.model.AuthSession
import com.poke.music.data.model.Profile
import com.poke.music.data.remote.SupabaseConfig
import com.poke.music.util.BadgeHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class AuthRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val json = "application/json".toMediaType()

    @Volatile var session: AuthSession? = null
        private set

    suspend fun login(email: String, password: String): Result<AuthSession> = withContext(Dispatchers.IO) {
        try {
            val body = gson.toJson(mapOf("email" to email, "password" to password))
            val req = Request.Builder()
                .url("${SupabaseConfig.KOKORO_URL}/auth/v1/token?grant_type=password")
                .addHeader("apikey", SupabaseConfig.KOKORO_ANON)
                .addHeader("Content-Type", "application/json")
                .post(body.toRequestBody(json))
                .build()
            client.newCall(req).execute().use { resp ->
                val text = resp.body?.string().orEmpty()
                if (!resp.isSuccessful) return@withContext Result.failure(Exception(parseError(text)))
                val obj = gson.fromJson(text, JsonObject::class.java)
                val access = obj.get("access_token")?.asString ?: return@withContext Result.failure(Exception("No token"))
                val refresh = obj.get("refresh_token")?.asString ?: ""
                val user = obj.getAsJsonObject("user")
                val userId = user?.get("id")?.asString ?: ""
                val userEmail = user?.get("email")?.asString ?: email
                val s = AuthSession(access, refresh, userId, userEmail)
                session = s
                Result.success(s)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(
        email: String,
        password: String,
        username: String,
        ownerCode: String? = null
    ): Result<AuthSession> = withContext(Dispatchers.IO) {
        try {
            val body = gson.toJson(mapOf("email" to email, "password" to password))
            val req = Request.Builder()
                .url("${SupabaseConfig.KOKORO_URL}/auth/v1/signup")
                .addHeader("apikey", SupabaseConfig.KOKORO_ANON)
                .addHeader("Content-Type", "application/json")
                .post(body.toRequestBody(json))
                .build()
            client.newCall(req).execute().use { resp ->
                val text = resp.body?.string().orEmpty()
                if (!resp.isSuccessful) return@withContext Result.failure(Exception(parseError(text)))
                val obj = gson.fromJson(text, JsonObject::class.java)
                // signup may return user without session if email confirm is on
                val access = obj.get("access_token")?.asString
                val user = obj.getAsJsonObject("user") ?: obj
                val userId = user.get("id")?.asString ?: ""
                val userEmail = user.get("email")?.asString ?: email

                if (access != null) {
                    val refresh = obj.get("refresh_token")?.asString ?: ""
                    val s = AuthSession(access, refresh, userId, userEmail)
                    session = s
                    createProfile(s, username, ownerCode)
                    Result.success(s)
                } else {
                    // try login after signup
                    login(email, password).also { result ->
                        result.getOrNull()?.let { createProfile(it, username, ownerCode) }
                    }
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun createProfile(session: AuthSession, username: String, ownerCode: String?) {
        withContext(Dispatchers.IO) {
            try {
                val badge = BadgeHelper.resolveBadge(session.email, ownerCode)
                val payload = mapOf(
                    "id" to session.userId,
                    "username" to username,
                    "display_name" to username,
                    "badge" to badge
                )
                val body = gson.toJson(payload)
                val req = Request.Builder()
                    .url("${SupabaseConfig.KOKORO_URL}/rest/v1/profiles")
                    .addHeader("apikey", SupabaseConfig.KOKORO_ANON)
                    .addHeader("Authorization", "Bearer ${session.accessToken}")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Prefer", "resolution=merge-duplicates")
                    .post(body.toRequestBody(json))
                    .build()
                client.newCall(req).execute().close()
            } catch (_: Exception) { }
        }
    }

    suspend fun getMyProfile(): Result<Profile> = withContext(Dispatchers.IO) {
        val s = session ?: return@withContext Result.failure(Exception("No session"))
        try {
            val url = "${SupabaseConfig.KOKORO_URL}/rest/v1/profiles?id=eq.${s.userId}&select=*"
            val req = Request.Builder()
                .url(url)
                .addHeader("apikey", SupabaseConfig.KOKORO_ANON)
                .addHeader("Authorization", "Bearer ${s.accessToken}")
                .get()
                .build()
            client.newCall(req).execute().use { resp ->
                val text = resp.body?.string().orEmpty()
                if (!resp.isSuccessful) return@withContext Result.failure(Exception("HTTP ${resp.code}"))
                val arr = gson.fromJson(text, Array<Profile>::class.java)
                Result.success(arr?.firstOrNull() ?: Profile(id = s.userId, displayName = s.email))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        session = null
    }

    private fun parseError(text: String): String {
        return try {
            gson.fromJson(text, JsonObject::class.java)?.get("msg")?.asString
                ?: gson.fromJson(text, JsonObject::class.java)?.get("error_description")?.asString
                ?: text.take(120)
        } catch (_: Exception) {
            text.take(120)
        }
    }
}
