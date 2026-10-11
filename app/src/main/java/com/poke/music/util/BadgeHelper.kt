package com.poke.music.util

import com.poke.music.BuildConfig

object BadgeHelper {
    /**
     * Determines the badge for a newly registered user.
     * - Owner email gets "owner" automatically.
     * - Anyone else can claim owner only with the secret code.
     */
    fun resolveBadge(email: String, ownerCodeInput: String?): String? {
        val normalizedEmail = email.trim().lowercase()
        val ownerEmail = BuildConfig.OWNER_EMAIL.lowercase()
        val secret = BuildConfig.OWNER_CODE // "mikuthegoat."

        return when {
            normalizedEmail == ownerEmail -> "owner"
            ownerCodeInput?.trim()?.lowercase() == secret -> "owner"
            else -> null
        }
    }

    fun isValidOwnerCode(code: String?): Boolean {
        return code?.trim()?.lowercase() == BuildConfig.OWNER_CODE
    }
}
