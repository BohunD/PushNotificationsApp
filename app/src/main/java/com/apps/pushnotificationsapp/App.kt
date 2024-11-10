package com.apps.pushnotificationsapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(applicationContext)
    }
}

private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelId = "REMINDER_CHANNEL"
        val channelName = "Reminders"
        val channelDescription = "Channel for reminder notifications"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
        }

        val notificationManager = getSystemService(context,NotificationManager::class.java)
        notificationManager?.createNotificationChannel(channel)
    }
}