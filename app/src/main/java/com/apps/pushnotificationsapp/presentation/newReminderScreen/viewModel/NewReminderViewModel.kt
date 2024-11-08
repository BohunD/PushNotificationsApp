package com.apps.pushnotificationsapp.presentation.newReminderScreen.viewModel

import androidx.lifecycle.ViewModel
import com.apps.videolibrary.mvi.UnidirectionalViewModel
import com.apps.videolibrary.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewReminderViewModel @Inject constructor(
) : ViewModel(), UnidirectionalViewModel<NewReminderContract.State, NewReminderContract.Event, NewReminderContract.Effect> by mvi(
    NewReminderContract.State(),
) {
    override fun event(event: NewReminderContract.Event) = when (event) {
        is NewReminderContract.Event.SetCurrentDay -> updateCurrentDay(event.day)
        is NewReminderContract.Event.SetCurrentHour -> updateCurrentHour(event.hour)
        is NewReminderContract.Event.SetCurrentMinute -> updateCurrentMinute(event.minute)
        is NewReminderContract.Event.SetCurrentMonth -> updateCurrentMonth(event.month)
        is NewReminderContract.Event.SetCurrentYear -> updateCurrentYear(event.year)
        is NewReminderContract.Event.SetCurrentRepeatMode -> updateUiState { copy(currentRepeatMode = event.repeatMode) }
        is NewReminderContract.Event.SetCurrentTitle -> updateUiState { copy(currentTitle = event.title) }

        NewReminderContract.Event.ValidateDay -> validateDay()
        NewReminderContract.Event.ValidateHour -> validateHour()
        NewReminderContract.Event.ValidateMinute -> validateMinute()
        NewReminderContract.Event.ValidateMonth -> validateMonth()
        NewReminderContract.Event.ValidateYear -> validateYear()

        NewReminderContract.Event.ValidateDate -> validateDate()
    }

    private fun updateCurrentHour(hour: String) {
        if (hour.isEmpty()) {
            updateUiState { copy(currentHour = hour) }
        } else {
            val sanitizedHour = hour.filter { it.isDigit() }.take(2)
            updateUiState { copy(currentHour = sanitizedHour) }
        }
        validateHour()
    }

    private fun validateHour() {
        val hourValue = state.value.currentHour.toIntOrNull()
        val isValid = hourValue != null && hourValue in 0..23
        updateUiState { copy(isHourError = !isValid) }
    }

    private fun updateCurrentMinute(minute: String) {
        if (minute.isEmpty()) {
            updateUiState { copy(currentMinute = minute) }
        } else {
            val sanitizedMinute = minute.filter { it.isDigit() }.take(2)
            updateUiState { copy(currentMinute = sanitizedMinute) }
        }
        validateMinute()
    }

    private fun validateMinute() {
        val minuteValue = state.value.currentMinute.toIntOrNull()
        val isValid = minuteValue != null && minuteValue in 0..59
        updateUiState { copy(isMinuteError = !isValid) }
    }

    private fun updateCurrentDay(day: String) {
        if (day.isEmpty()) {
            updateUiState { copy(currentDay = day) }
        } else {
            val sanitizedDay = day.filter { it.isDigit() }.take(2)
            updateUiState { copy(currentDay = sanitizedDay) }
        }
        validateDay()
    }

    private fun validateDay() {
        val dayValue = state.value.currentDay.toIntOrNull()
        val isValid = dayValue != null && dayValue in 1..31
        updateUiState { copy(isDayError = !isValid) }
    }

    private fun updateCurrentMonth(month: String) {
        if (month.isEmpty()) {
            updateUiState { copy(currentMonth = month) }
        } else {
            val sanitizedMonth = month.filter { it.isDigit() }.take(2)
            updateUiState { copy(currentMonth = sanitizedMonth) }
        }
        validateMonth()
    }

    private fun validateMonth() {
        val monthValue = state.value.currentMonth.toIntOrNull()
        val isValid = monthValue != null && monthValue in 1..12
        updateUiState { copy(isMonthError = !isValid) }
    }

    private fun updateCurrentYear(year: String) {
        if (year.isEmpty()) {
            updateUiState { copy(currentYear = year) }
        } else {
            val sanitizedYear = year.filter { it.isDigit() }.take(4)
            updateUiState { copy(currentYear = sanitizedYear) }
        }
        validateYear()
    }

    private fun validateYear() {
        val yearValue = state.value.currentYear.toIntOrNull()
        val isValid = yearValue != null && yearValue in 1900..2100
        updateUiState { copy(isYearError = !isValid) }
    }

    private fun validateDate() {
        val currentDateTime = System.currentTimeMillis()

        val selectedDateTime = with(state.value) {
            val selectedYear = currentYear.toIntOrNull() ?: 0
            val selectedMonth = currentMonth.toIntOrNull() ?: 0
            val selectedDay = currentDay.toIntOrNull() ?: 0
            val selectedHour = currentHour.toIntOrNull() ?: 0
            val selectedMinute = currentMinute.toIntOrNull() ?: 0

            val calendar = java.util.Calendar.getInstance().apply {
                set(selectedYear, selectedMonth - 1, selectedDay, selectedHour, selectedMinute, 0)
            }

            calendar.timeInMillis
        }

        val isDateValid = selectedDateTime >= currentDateTime

        updateUiState {
            copy(isDateError = !isDateValid)
        }
    }
}


