package com.apps.pushnotificationsapp.presentation.mainScreen.viewmodel

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
                if (reminder.cancellationDate == today) {
                    reminder.copy(isCancelledToday = true)
                } else {
                    reminder.copy(cancellationDate = null)
                }
            }
            withContext(Dispatchers.Main) {
                updateUiState { copy(currentRemindersList = list) }
            }
        }
    }

}