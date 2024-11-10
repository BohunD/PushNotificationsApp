package com.apps.pushnotificationsapp.domain.repository

import com.apps.pushnotificationsapp.domain.model.Reminder

interface ReminderRepository {
    suspend fun addReminder(reminder: Reminder)
    suspend fun updateReminder(reminder: Reminder)
    suspend fun deleteReminder(reminder: Reminder)
    suspend fun getReminderById(id: Int): Reminder?
    suspend fun getAllReminders(): List<Reminder>
    suspend fun cancelNotificationForToday(reminder: Reminder, today: String)
}