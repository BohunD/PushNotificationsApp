package com.apps.pushnotificationsapp.presentation.newReminderScreen.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.apps.pushnotificationsapp.presentation.newReminderScreen.viewModel.NewReminderContract
import com.apps.pushnotificationsapp.presentation.newReminderScreen.viewModel.NewReminderViewModel
import com.apps.videolibrary.mvi.use

@Composable
fun NewReminderScreen(id: Int, onCloseScreen: () -> Unit) {
    val viewModel = hiltViewModel<NewReminderViewModel>()
    val (state, event) = use(viewModel)
    val context = LocalContext.current
    LaunchedEffect(state.dateError) {
        if (state.dateError != null) {
            Toast.makeText(
                context,
                state.dateError,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    LaunchedEffect(id) {
        if(id!=-1) {
            event(NewReminderContract.Event.GetReminderInfo(id))
        }
        event(NewReminderContract.Event.SetReminderId(id))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Header(onCloseScreen, event, state)
        Spacer(modifier = Modifier.weight(0.8f))
        ReminderInputBox(state, event)
        Spacer(modifier = Modifier.weight(1f))
    }
}






