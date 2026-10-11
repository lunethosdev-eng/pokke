package com.poke.music.data.repository

import android.content.Context
import com.poke.music.data.model.DownloadProgress
import com.poke.music.data.model.SearchResponse
import com.poke.music.data.model.Song
import com.poke.music.data.remote.MusicApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream

class MusicRepository(private val context: Context) {

    private val api get() = MusicApiClient.create()
    private val downloadClient = OkHttpClient()

    private val _downloadProgress = MutableStateFlow<DownloadProgress?>(null)
    val downloadProgress: Flow<DownloadProgress?> = _downloadProgress.asStateFlow()

    /**
     * Search songs.
     * The Cloudflare endpoint already checks the database first and only
     * downloads on-demand when the song is missing.
     */
    suspend fun search(query: String): Result<List<Song>> = withContext(Dispatchers.IO) {
        try {
            val response = api.search(query.trim())
            Result.success(response.results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Download a song to internal storage (or user-chosen path later).
     * Emits progress 0-100.
     */
    suspend fun downloadSong(
        song: Song,
        destinationDir: File? = null
    ): Result<File> = withContext(Dispatchers.IO) {
        try {
            val dir = destinationDir ?: File(context.filesDir, "music").also { it.mkdirs() }
            val fileName = "${song.youtubeId ?: song.id}.mp3"
            val outFile = File(dir, fileName)

            if (outFile.exists() && outFile.length() > 0) {
                _downloadProgress.value = DownloadProgress(song.id, 100, true)
                return@withContext Result.success(outFile)
            }

            val request = Request.Builder().url(song.audioUrl).build()
            downloadClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw Exception("HTTP ${response.code}")
                }
                val body = response.body ?: throw Exception("Empty body")
                val total = body.contentLength()
                var downloaded = 0L

                body.byteStream().use { input ->
                    FileOutputStream(outFile).use { output ->
                        val buffer = ByteArray(8 * 1024)
                        var read: Int
                        while (input.read(buffer).also { read = it } != -1) {
                            output.write(buffer, 0, read)
                            downloaded += read
                            val percent = if (total > 0) {
                                ((downloaded * 100) / total).toInt().coerceIn(0, 100)
                            } else 0
                            _downloadProgress.value = DownloadProgress(song.id, percent)
                        }
                        output.flush()
                    }
                }
            }

            _downloadProgress.value = DownloadProgress(song.id, 100, true)
            Result.success(outFile)
        } catch (e: Exception) {
            _downloadProgress.value = DownloadProgress(song.id, 0, false, e.message)
            Result.failure(e)
        }
    }

    fun clearDownloadProgress() {
        _downloadProgress.value = null
    }
}
