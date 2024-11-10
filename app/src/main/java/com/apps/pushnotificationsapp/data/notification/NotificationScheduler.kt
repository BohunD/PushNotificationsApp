package com.apps.pushnotificationsapp.data.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.apps.pushnotificationsapp.domain.model.Reminder
import com.apps.pushnotificationsapp.domain.model.RepeatMode
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
class NotificationScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun cancelNotificationForToday(reminder: Reminder) {
        cancelNotification(reminder.id)

        val nextDate = when (reminder.repeatMode) {
            RepeatMode.DAILY.displayName -> getNextDateForDaily()
            RepeatMode.MON_TO_FRI.displayName -> getNextDateForMonToFri()
            else -> null
        }

        if (nextDate != null) {
            val newReminder = reminder.copy(date = nextDate)
            scheduleNotification(newReminder)
        }
    }

    private fun getNextDateForDaily(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 1)
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
    }


    private fun getNextDateForMonToFri(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 1)

        while (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, 1)
        }

        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
    }

    fun scheduleNotification(reminder: Reminder) {
        if(reminder.date.isNotEmpty() && reminder.time.isNotEmpty()) {
            val intent = Intent(context, NotificationReceiver::class.java).apply {
                putExtra("title", reminder.title)
                putExtra("id", reminder.id)
                putExtra("date", reminder.date)
                putExtra("time", reminder.time)
                putExtra("repeatMode", reminder.repeatMode)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                reminder.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val calendar = Calendar.getInstance().apply {
                time = SimpleDateFormat(
                    "dd/MM/yyyy HH:mm",
                    Locale.getDefault()
                ).parse("${reminder.date} ${reminder.time}")!!
                if (before(Calendar.getInstance())) {
                    add(Calendar.DATE, 1)
                }
            }

            Log.d("NotificationScheduler", "Notification scheduled at: ${calendar.time}")

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    fun cancelNotification(reminderId: Int) {
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminderId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        Log.d("NotificationScheduler", "Notification canceled for ID: $reminderId")
    }
}
