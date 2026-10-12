package com.poke.music.data.model

import com.google.gson.annotations.SerializedName

data class Song(
    @SerializedName("id") val id: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("artist") val artist: String = "",
    @SerializedName("album") val album: String? = null,
    @SerializedName("duration_seconds") val durationSeconds: Int = 0,
    @SerializedName("audio_url") val audioUrl: String = "",
    @SerializedName("cover_url") val coverUrl: String? = null,
    @SerializedName("animated_cover_url") val animatedCoverUrl: String? = null,
    @SerializedName("lyrics_text") val lyricsText: String? = null,
    @SerializedName("genre") val genre: String? = null,
    @SerializedName("youtube_id") val youtubeId: String? = null
)
