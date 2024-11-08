package com.apps.pushnotificationsapp.presentation.newReminderScreen.viewModel

import androidx.compose.runtime.Immutable

interface NewReminderContract {

    @Immutable
    data class State(
        val currentTitle: String = "",
        val currentHour: String = "",
        val currentMinute: String = "",
        val currentDay: String = "",
        val currentMonth: String = "",
        val currentYear: String = "",
        val currentRepeatMode: String = "",

        val isHourError: Boolean = false,
        val isMinuteError: Boolean = false,
        val isDayError: Boolean = false,
        val isMonthError: Boolean = false,
        val isYearError: Boolean = false,
        val isDateError: Boolean? = null,
    )

    sealed interface Event {
        data class SetCurrentTitle(val title: String): Event
        data class SetCurrentHour(val hour: String): Event
        data class SetCurrentMinute(val minute: String): Event
        data class SetCurrentDay(val day: String): Event
        data class SetCurrentMonth(val month: String): Event
        data class SetCurrentYear(val year: String): Event
        data class SetCurrentRepeatMode(val repeatMode: String): Event

        data object ValidateHour: Event
        data object ValidateMinute: Event
        data object ValidateDay: Event
        data object ValidateMonth: Event
        data object ValidateYear: Event
        data object ValidateDate: Event
    }

    sealed interface Effect {
    }
}