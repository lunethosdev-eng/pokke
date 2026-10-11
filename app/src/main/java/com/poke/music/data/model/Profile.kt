package com.poke.music.data.model

import com.google.gson.annotations.SerializedName

data class Profile(
    val id: String,
    @SerializedName("display_name") val displayName: String? = null,
    val username: String? = null,
    val bio: String? = null,
    @SerializedName("avatar_url") val avatarUrl: String? = null,
    @SerializedName("banner_url") val bannerUrl: String? = null,
    @SerializedName("photo_uri") val photoUri: String? = null,
    @SerializedName("banner_uri") val bannerUri: String? = null,
    @SerializedName("is_public") val isPublic: Boolean = true,
    @SerializedName("show_listening") val showListening: Boolean = true,
    @SerializedName("show_playlists") val showPlaylists: Boolean = true,
    @SerializedName("show_favorites") val showFavorites: Boolean = true,
    @SerializedName("allow_messages") val allowMessages: Boolean = true,
    @SerializedName("followers_count") val followersCount: Int = 0,
    @SerializedName("following_count") val followingCount: Int = 0,
    @SerializedName("is_artist") val isArtist: Boolean = false,
    val badge: String? = null, // "owner", "admin", "artist", null
    @SerializedName("visits_count") val visitsCount: Int = 0,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("updated_at") val updatedAt: String? = null
) {
    val isOwner: Boolean get() = badge.equals("owner", ignoreCase = true)
    val isAdmin: Boolean get() = badge.equals("admin", ignoreCase = true) || isOwner
}
