package me.pulkitkumar.foregroundServiceQuitOnAppClose

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class App : Application(){

    companion object {
        public val CHANNEL_ID: String = "bound-service-channel"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "Bound Service Name",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationChannel.setSound(null, null)
            notificationChannel.importance = NotificationManager.IMPORTANCE_LOW
            notificationChannel.enableLights(false)
            notificationChannel.enableVibration(false)
            notificationChannel.setShowBadge(false)
            val notificationManager : NotificationManager = getSystemService(NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}