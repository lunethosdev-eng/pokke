package com.poke.music.data.model

import com.google.gson.annotations.SerializedName

data class Song(
    val id: String,
    val isrc: String? = null,
    @SerializedName("youtube_id") val youtubeId: String? = null,
    val title: String,
    val artist: String,
    val album: String? = null,
    @SerializedName("duration_seconds") val durationSeconds: Int = 0,
    @SerializedName("audio_url") val audioUrl: String,
    @SerializedName("cover_url") val coverUrl: String? = null,
    @SerializedName("animated_cover_url") val animatedCoverUrl: String? = null,
    @SerializedName("lyrics_url") val lyricsUrl: String? = null,
    @SerializedName("lyrics_text") val lyricsText: String? = null,
    val genre: String? = null,
    @SerializedName("release_year") val releaseYear: Int? = null,
    @SerializedName("source_platform") val sourcePlatform: String? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("updated_at") val updatedAt: String? = null,
    // Local only
    val isDownloaded: Boolean = false,
    val localPath: String? = null
) {
    val durationFormatted: String
        get() {
            val m = durationSeconds / 60
            val s = durationSeconds % 60
            return "%d:%02d".format(m, s)
        }
}

data class SearchResponse(
    val source: String? = null,
    val results: List<Song> = emptyList()
)

data class DownloadProgress(
    val songId: String,
    val progress: Int, // 0-100
    val isComplete: Boolean = false,
    val error: String? = null
)
