package com.apps.pushnotificationsapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.apps.pushnotificationsapp.data.model.ReminderEntity

@Database(entities = [ReminderEntity::class], version = 1, exportSchema = false)
abstract class ReminderDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
}
