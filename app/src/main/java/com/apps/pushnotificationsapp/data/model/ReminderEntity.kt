package com.apps.pushnotificationsapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val time: String,
    val date: String,
    val repeatMode: String,
    val cancellationDate: String? = null
)
