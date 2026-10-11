package com.poke.music.data.remote

import com.poke.music.BuildConfig
import com.poke.music.data.model.SearchResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface MusicApi {
    @GET("api/search")
    suspend fun search(
        @Query("q") query: String
    ): SearchResponse
}

object MusicApiClient {
    @Volatile
    var baseUrl: String = BuildConfig.MUSIC_API_BASE

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    fun create(): MusicApi {
        return Retrofit.Builder()
            .baseUrl(baseUrl.ensureTrailingSlash())
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MusicApi::class.java)
    }

    private fun String.ensureTrailingSlash(): String =
        if (endsWith("/")) this else "$this/"
}
