package com.apps.pushnotificationsapp.presentation.newReminderScreen.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.pushnotificationsapp.domain.model.Reminder
import com.apps.pushnotificationsapp.domain.repository.ReminderRepository
import com.apps.videolibrary.mvi.UnidirectionalViewModel
import com.apps.videolibrary.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NewReminderViewModel @Inject constructor(
    private val repository: ReminderRepository,
) : ViewModel(),
    UnidirectionalViewModel<NewReminderContract.State, NewReminderContract.Event, NewReminderContract.Effect> by mvi(
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
        NewReminderContract.Event.SaveReminder -> saveReminder()

        is NewReminderContract.Event.GetReminderInfo -> getReminderInfo(event.id)

        is NewReminderContract.Event.SetReminderId -> {
            updateUiState { copy(reminderId = event.id) }
        }
    }

    private fun getReminderInfo(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val reminder = repository.getReminderById(id)
                reminder?.let {
                    val (hour, minute) = it.time.split(":")
                    val (day, month, year) = it.date.split("/")

                    Log.d("GET_REMINDER", "MODE: ${reminder.repeatMode}")
                    withContext(Dispatchers.Main) {
                        updateUiState {
                            copy(
                                currentHour = hour,
                                currentMinute = minute,
                                currentDay = day,
                                currentMonth = month,
                                currentYear = year,
                                currentRepeatMode = it.repeatMode,
                                currentTitle = it.title,
                                isDayError = false,
                                isMonthError = false,
                                isYearError = false,
                                isMinuteError = false,
                                isHourError = false,
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("Error getReminderInfo: ", e.message.toString())
            }
        }
    }


    private fun saveReminder() {
        with(state.value) {
            val hour = currentHour.ifEmpty { "00" }
            val minute = currentMinute.ifEmpty { "00" }
            if (currentMinute.isEmpty())
                updateUiState { copy(currentMinute = "00") }
            val time = "$hour:$minute"

            val date = "$currentDay/$currentMonth/$currentYear"
            val reminder = if(reminderId ==-1)
            Reminder(
                title = currentTitle,
                time = time,
                date = date,
                repeatMode = currentRepeatMode
            ) else
                Reminder(
                    id = state.value.reminderId,
                    title = currentTitle,
                    time = time,
                    date = date,
                    repeatMode = currentRepeatMode
                )
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    if(state.value.reminderId!=-1){
                        repository.updateReminder(reminder)
                    }else
                        repository.addReminder(reminder)
                    withContext(Dispatchers.Main) {
                        updateUiState { copy(isSaved = true) }
                    }
                } catch (e: Exception) {
                    Log.e("Error saveReminder:", e.message.toString())
                }
            }
        }
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
        val isValid = hourValue == null || hourValue in 0..23
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
        val isValid = minuteValue == null || minuteValue in 0..59
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

        val isDateValid =
            ((selectedDateTime >= currentDateTime) && !state.value.isMinuteError && !state.value.isHourError && !state.value.isDayError && !state.value.isMonthError && !state.value.isYearError)
        Log.d("IS_SAVED_CLICKED_VALIDATE", isDateValid.toString())
        updateUiState {
            copy(isDateError = !isDateValid)
        }
        saveReminder()
    }
}


