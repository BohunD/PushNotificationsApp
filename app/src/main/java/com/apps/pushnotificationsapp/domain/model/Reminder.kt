package com.apps.pushnotificationsapp.domain.model

import com.apps.pushnotificationsapp.data.model.ReminderEntity

data class Reminder(
    val id: Int = 0,
    val title: String,
    val time: String,
    val date: String,
    val repeatMode: String
) {
    fun toEntity(): ReminderEntity {
        return ReminderEntity(
            id = id,
            title = title,
            time = time,
            date = date,
            repeatMode = repeatMode
        )
    }

    companion object {
        fun fromEntity(entity: ReminderEntity): Reminder {
            return Reminder(
                id = entity.id,
                title = entity.title,
                time = entity.time,
                date = entity.date,
                repeatMode = entity.repeatMode
            )
        }
    }
}
