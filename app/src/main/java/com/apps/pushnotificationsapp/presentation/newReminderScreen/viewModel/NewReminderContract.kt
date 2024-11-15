package com.apps.pushnotificationsapp.presentation.newReminderScreen.viewModel

import androidx.compose.runtime.Immutable
import com.apps.pushnotificationsapp.domain.model.RepeatMode

interface NewReminderContract {

    @Immutable
    data class State(
        val currentTitle: String = "",
        val currentHour: String = "",
        val currentMinute: String = "",
        val currentDay: String = "",
        val currentMonth: String = "",
        val currentYear: String = "",
        val currentRepeatMode: String = RepeatMode.ONCE.displayName,

        val isHourError: Boolean = false,
        val isMinuteError: Boolean = false,
        val isDayError: Boolean = false,
        val isMonthError: Boolean = false,
        val isYearError: Boolean = false,
        val dateError: String? = null,
        val isSaved: Boolean? = null,

        val reminderId: Int = -1
    )

    sealed interface Event {
        data class SetCurrentTitle(val title: String): Event
        data class SetCurrentHour(val hour: String): Event
        data class SetCurrentMinute(val minute: String): Event
        data class SetCurrentDay(val day: String): Event
        data class SetCurrentMonth(val month: String): Event
        data class SetCurrentYear(val year: String): Event
        data class SetCurrentRepeatMode(val repeatMode: String): Event

        data class SetReminderId(val id: Int): Event
        data class GetReminderInfo(val id: Int): Event

        data object ValidateHour: Event
        data object ValidateMinute: Event
        data object ValidateDay: Event
        data object ValidateMonth: Event
        data object ValidateYear: Event
        data object ValidateDate: Event

        data object SaveReminder: Event
    }

    sealed interface Effect {
    }

}
