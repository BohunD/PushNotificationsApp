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
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repository: ReminderRepository
) : ViewModel(), UnidirectionalViewModel<MainScreenContract.State, MainScreenContract.Event, MainScreenContract.Effect> by mvi(
    MainScreenContract.State(),
) {
    override fun event(event: MainScreenContract.Event) = when(event) {
        is MainScreenContract.Event.CancelReminder -> TODO()
        is MainScreenContract.Event.DeleteReminder -> deleteReminder(event.reminder)
        is MainScreenContract.Event.EditReminder -> editReminder(event.reminder)

        MainScreenContract.Event.FetchRemindersFromDB -> fetchFromDB()
    }

    private fun deleteReminder( reminder: Reminder){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteReminder(reminder)
            fetchFromDB()
        }
    }

    private fun editReminder(reminder: Reminder){

    }

    private fun fetchFromDB(){
        viewModelScope.launch(Dispatchers.IO) {
            val list = repository.getAllReminders()
            withContext(Dispatchers.Main){
                updateUiState { copy(currentRemindersList = list) }
            }
        }
    }
}