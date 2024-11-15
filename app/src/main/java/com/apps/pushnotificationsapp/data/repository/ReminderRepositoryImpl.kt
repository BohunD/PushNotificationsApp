package com.apps.pushnotificationsapp.data.repository

import com.apps.pushnotificationsapp.data.db.ReminderDao
import com.apps.pushnotificationsapp.data.notification.NotificationScheduler
import com.apps.pushnotificationsapp.domain.model.Reminder
import com.apps.pushnotificationsapp.domain.repository.ReminderRepository

class ReminderRepositoryImpl(
    private val reminderDao: ReminderDao,
    private val notificationScheduler: NotificationScheduler
) : ReminderRepository {

    override suspend fun addReminder(reminder: Reminder) {
        reminderDao.insertReminder(reminder.toEntity())
        notificationScheduler.scheduleNotification(reminder)
    }

    override suspend fun updateReminder(reminder: Reminder) {
        reminderDao.updateReminder(reminder.toEntity())
        notificationScheduler.cancelNotification(reminder.id)
        notificationScheduler.scheduleNotification(reminder)
    }

    override suspend fun deleteReminder(reminder: Reminder) {
        reminderDao.deleteReminder(reminder.toEntity())
        notificationScheduler.cancelNotification(reminder.id)
    }

    override suspend fun getReminderById(id: Int): Reminder? {
        return reminderDao.getReminderById(id)?.let { Reminder.fromEntity(it) }
    }

    override suspend fun getAllReminders(): List<Reminder> {
        return reminderDao.getAllReminders().map { Reminder.fromEntity(it) }
    }

    override suspend fun cancelNotificationForToday(reminder: Reminder, today: String) {
        notificationScheduler.cancelNotificationForToday(reminder)
    }

}
