package com.poke.music.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.poke.music.data.model.Song
import com.poke.music.data.remote.SupabaseConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

class MusicRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()

    suspend fun getCatalog(limit: Int = 50): Result<List<Song>> = withContext(Dispatchers.IO) {
        try {
            val url = "${SupabaseConfig.UKIYO_URL}/rest/v1/songs?select=*&order=created_at.desc&limit=$limit"
            val req = Request.Builder()
                .url(url)
                .addHeader("apikey", SupabaseConfig.UKIYO_ANON)
                .addHeader("Authorization", "Bearer ${SupabaseConfig.UKIYO_ANON}")
                .get()
                .build()
            client.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) return@withContext Result.failure(Exception("HTTP ${resp.code}"))
                val body = resp.body?.string().orEmpty()
                val type = object : TypeToken<List<Song>>() {}.type
                val list: List<Song> = gson.fromJson(body, type) ?: emptyList()
                Result.success(list)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchLocal(query: String): Result<List<Song>> = withContext(Dispatchers.IO) {
        try {
            val q = URLEncoder.encode("%$query%", "UTF-8")
            val url =
                "${SupabaseConfig.UKIYO_URL}/rest/v1/songs?or=(title.ilike.$q,artist.ilike.$q)&select=*&limit=30"
            val req = Request.Builder()
                .url(url)
                .addHeader("apikey", SupabaseConfig.UKIYO_ANON)
                .addHeader("Authorization", "Bearer ${SupabaseConfig.UKIYO_ANON}")
                .get()
                .build()
            client.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) return@withContext Result.failure(Exception("HTTP ${resp.code}"))
                val body = resp.body?.string().orEmpty()
                val type = object : TypeToken<List<Song>>() {}.type
                val list: List<Song> = gson.fromJson(body, type) ?: emptyList()
                Result.success(list)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** On-demand fetch via Cloudflare tunnel when song is not in catalog */
    suspend fun searchRemote(query: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val q = URLEncoder.encode(query, "UTF-8")
            val base = SupabaseConfig.musicApiBase.trimEnd('/')
            val url = "$base/api/search?q=$q"
            val req = Request.Builder().url(url).get().build()
            client.newCall(req).execute().use { resp ->
                val body = resp.body?.string().orEmpty()
                if (!resp.isSuccessful) return@withContext Result.failure(Exception("HTTP ${resp.code}: $body"))
                Result.success(body)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
