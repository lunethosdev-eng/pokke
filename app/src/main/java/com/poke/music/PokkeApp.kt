package com.poke.music

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class PokkeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val musicChannel = NotificationChannel(
                CHANNEL_MUSIC,
                "Reproducción de música",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Controles de reproducción en segundo plano"
                setShowBadge(false)
            }

            val generalChannel = NotificationChannel(
                CHANNEL_GENERAL,
                "Notificaciones generales",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Anuncios y mensajes"
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(musicChannel)
            manager.createNotificationChannel(generalChannel)
        }
    }

    companion object {
        const val CHANNEL_MUSIC = "pokke_music"
        const val CHANNEL_GENERAL = "pokke_general"
    }
}
