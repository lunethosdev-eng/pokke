package com.poke.music.data.remote

object SupabaseConfig {
    // Kokoro = auth + profiles + social
    const val KOKORO_URL = "https://ajbmpgnzkgtcmulocftd.supabase.co"
    const val KOKORO_ANON =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImFqYm1wZ256a2d0Y211bG9jZnRkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3OTAzNDU2MjksImV4cCI6MjEwNTkyMTYyOX0.twsBnT9WBBILyXFuoQh7viI578yoNbeQztKHsr8DB8k"

    // Ukiyo = music catalog (songs, audio)
    const val UKIYO_URL = "https://esjoifsjljvymttinyhj.supabase.co"
    const val UKIYO_ANON =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVzam9pZnNqbGp2eW10dGlueWhqIiwicm9sZSI6ImFub24iLCJpYXQiOjE3OTEwMzYyMTAsImV4cCI6MjEwNjYxMjIxMH0.GKA3KyoMElVwLUQlL2ad82IPjRmwKmMJEGPTUNwbuVQ"

    // Cloudflare tunnel for on-demand search/download
    @Volatile
    var musicApiBase: String = "https://sol-deeply-feature-administration.trycloudflare.com"

    const val OWNER_EMAIL = "lunethos.dev@gmail.com"
    const val OWNER_CODE = "mikuthegoat."
}
