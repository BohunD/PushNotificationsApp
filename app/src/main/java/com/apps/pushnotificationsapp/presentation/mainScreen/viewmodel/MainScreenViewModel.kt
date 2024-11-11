package com.apps.pushnotificationsapp.presentation.mainScreen.viewmodel

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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repository: ReminderRepository
) : ViewModel(), UnidirectionalViewModel<MainScreenContract.State, MainScreenContract.Event, MainScreenContract.Effect> by mvi(
    MainScreenContract.State(),
) {
    override fun event(event: MainScreenContract.Event) = when(event) {
        is MainScreenContract.Event.CancelReminder -> cancelReminderForToday(event.reminder)
        is MainScreenContract.Event.DeleteReminder -> deleteReminder(event.reminder)

        MainScreenContract.Event.FetchRemindersFromDB -> fetchFromDB()
    }

    private fun cancelReminderForToday(reminder: Reminder){
        viewModelScope.launch(Dispatchers.IO) {
            val cancelledReminder = reminder.copy(cancellationDate = state.value.todayDate)
            repository.updateReminder(cancelledReminder)
            repository.cancelNotificationForToday(reminder, state.value.todayDate)
            fetchFromDB()
        }
    }

    private fun deleteReminder( reminder: Reminder){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteReminder(reminder)
            fetchFromDB()
        }
    }


    private fun fetchFromDB() {
        val today = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        updateUiState{copy(todayDate = today)}
        viewModelScope.launch(Dispatchers.IO) {
            val list = repository.getAllReminders().map { reminder ->
                val formattedDate = formatDate(reminder.date)
                val formattedTime = formatTime(reminder.time)
                if (reminder.cancellationDate == today) {
                    reminder.copy(
                        isCancelledToday = true,
                        date = formattedDate,
                        time = formattedTime
                    )
                } else {
                    reminder.copy(
                        cancellationDate = null,
                        date = formattedDate,
                        time = formattedTime
                        )
                }
            }
            withContext(Dispatchers.Main) {
                updateUiState { copy(currentRemindersList = list) }
            }
        }
    }

    private fun formatDate(date: String): String {
        val parts = date.split("/")
        if (parts.size != 3) return date

        val day = String.format(Locale.getDefault(), "%02d", parts[0].toInt())
        val month = String.format(Locale.getDefault(), "%02d", parts[1].toInt())
        val year = parts[2]

        return "$day/$month/$year"
    }

    private fun formatTime(time: String): String {
        try {
            val parts = time.split(":")
            if (parts.size != 2) return time
            val hour = String.format(Locale.getDefault(), "%02d", parts[0].toInt())
            val minute = String.format(Locale.getDefault(), "%02d", parts[1].toInt())

            return "$hour:$minute"
        } catch (e: Exception) {
            Log.e("Error formatTime:", e.message.toString())
            return ""
        }
    }

}