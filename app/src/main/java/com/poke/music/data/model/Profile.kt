package com.poke.music.data.model

import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("id") val id: String = "",
    @SerializedName("username") val username: String? = null,
    @SerializedName("display_name") val displayName: String? = null,
    @SerializedName("avatar_url") val avatarUrl: String? = null,
    @SerializedName("banner_url") val bannerUrl: String? = null,
    @SerializedName("bio") val bio: String? = null,
    @SerializedName("badge") val badge: String? = "none",
    @SerializedName("is_artist") val isArtist: Boolean? = false,
    @SerializedName("is_public") val isPublic: Boolean? = true
)

data class AuthSession(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
    val email: String
)
