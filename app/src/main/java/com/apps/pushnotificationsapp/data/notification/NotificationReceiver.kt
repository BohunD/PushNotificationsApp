package com.apps.pushnotificationsapp.data.notification

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.apps.pushnotificationsapp.R
import com.apps.pushnotificationsapp.domain.model.RepeatMode
import com.apps.pushnotificationsapp.presentation.MainActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val EXTRA_TITLE = "title"

private const val EXTRA_ID = "id"

private const val EXTRA_DATE = "date"

private const val EXTRA_TIME = "time"

private const val EXTRA_REPEAT_MODE = "repeatMode"

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra(EXTRA_TITLE) ?: "Reminder"
        val notificationId = intent.getIntExtra(EXTRA_ID, 0)
        val date = intent.getStringExtra(EXTRA_DATE)
        val time = intent.getStringExtra(EXTRA_TIME)
        val repeatMode = intent.getStringExtra(EXTRA_REPEAT_MODE)

        Log.d(
            "NotificationReceiver",
            "Received notification for ID: $notificationId, repeatMode: $repeatMode"
        )

        val mainIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingMainIntent = PendingIntent.getActivity(
            context,
            0,
            mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "REMINDER_CHANNEL")
            .setContentTitle(title)
            .setContentText("Hurry up! Don’t forget your task :)")
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingMainIntent)
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(notificationId, notification)
        }

        if (date != null && time != null) {
            val nextAlarmTime = Calendar.getInstance().apply {
                this.time =
                    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).parse("$date $time")!!
            }

            when (repeatMode) {
                RepeatMode.DAILY.displayName -> nextAlarmTime.add(Calendar.DATE, 1)
                RepeatMode.MON_TO_FRI.displayName -> {
                    nextAlarmTime.add(Calendar.DATE, 1)
                    while (nextAlarmTime.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
                        nextAlarmTime.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
                    ) {
                        nextAlarmTime.add(Calendar.DATE, 1)
                    }
                }

                RepeatMode.ONCE.displayName -> return
            }

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val newIntent = Intent(context, NotificationReceiver::class.java).apply {
                putExtra(EXTRA_TITLE, title)
                putExtra(EXTRA_ID, notificationId)
                putExtra(
                    EXTRA_DATE,
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(nextAlarmTime.time)
                )
                putExtra(EXTRA_TIME, time)
                putExtra(EXTRA_REPEAT_MODE, repeatMode)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                notificationId,
                newIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                nextAlarmTime.timeInMillis,
                pendingIntent
            )

            Log.d(
                "NotificationReceiver",
                "Next notification scheduled at: ${nextAlarmTime.time} for repeatMode: $repeatMode"
            )
        }
    }
}
