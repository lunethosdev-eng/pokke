package com.poke.music.util

import com.poke.music.data.remote.SupabaseConfig

object BadgeHelper {
    fun resolveBadge(email: String, ownerCode: String? = null): String {
        if (email.equals(SupabaseConfig.OWNER_EMAIL, ignoreCase = true)) {
            return "owner"
        }
        if (ownerCode != null && ownerCode.lowercase() == SupabaseConfig.OWNER_CODE) {
            return "owner"
        }
        return "none"
    }
}
