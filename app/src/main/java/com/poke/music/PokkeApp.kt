package com.poke.music

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class PokkeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "pokke_playback",
                "Reproducción",
                NotificationManager.IMPORTANCE_LOW
            )
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)
        }
    }
}
