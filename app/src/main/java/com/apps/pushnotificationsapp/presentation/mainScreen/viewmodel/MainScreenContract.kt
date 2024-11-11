package com.apps.pushnotificationsapp.presentation.mainScreen.viewmodel

import androidx.compose.runtime.Immutable
import com.apps.pushnotificationsapp.domain.model.Reminder

interface MainScreenContract {

    @Immutable
    data class State(
        val currentRemindersList: List<Reminder>?=null,
        val todayDate: String = ""
    )

    sealed interface Event {
        data class DeleteReminder(val reminder: Reminder): Event
        data class CancelReminder(val reminder: Reminder): Event

        data object FetchRemindersFromDB: Event
    }

    sealed interface Effect {
    }
}
