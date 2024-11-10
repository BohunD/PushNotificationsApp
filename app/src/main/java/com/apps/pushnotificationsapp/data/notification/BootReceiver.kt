package com.apps.pushnotificationsapp.data.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.apps.pushnotificationsapp.domain.repository.ReminderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver(
    private val notificationScheduler: NotificationScheduler,
    private val repository: ReminderRepository,
) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            CoroutineScope(Dispatchers.IO).launch {
                val reminders = repository.getAllReminders()
                reminders.forEach { reminder ->
                    notificationScheduler.scheduleNotification(reminder)
                }
            }
        }
    }
}
