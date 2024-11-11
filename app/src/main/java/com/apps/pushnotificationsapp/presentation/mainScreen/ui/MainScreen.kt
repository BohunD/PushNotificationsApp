package com.apps.pushnotificationsapp.presentation.mainScreen.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.apps.pushnotificationsapp.R
import com.apps.pushnotificationsapp.presentation.mainScreen.ui.internalViews.Header
import com.apps.pushnotificationsapp.presentation.mainScreen.ui.internalViews.RemindersList
import com.apps.pushnotificationsapp.presentation.mainScreen.viewmodel.MainScreenContract
import com.apps.pushnotificationsapp.presentation.mainScreen.viewmodel.MainScreenViewModel
import com.apps.videolibrary.mvi.use

@Composable
fun MainScreen(
    onNewReminderClicked: () -> Unit,
    onEditReminderClicked: (Int) -> Unit,
) {
    val viewModel = hiltViewModel<MainScreenViewModel>()
    val (state, event) = use(viewModel)
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                event(MainScreenContract.Event.FetchRemindersFromDB)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    var shouldBlur by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .then(if (shouldBlur) Modifier.blur(2.dp) else Modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        state.currentRemindersList?.let {
            Header(
                it.isEmpty(),
                onAddTaskClick = { onNewReminderClicked() })
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            HorizontalDivider(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .align(Alignment.TopStart)
                    .background(
                        colorResource(id = R.color.black_text)
                    )
            )

            if (state.currentRemindersList == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colorResource(id = R.color.blue_9))
                }
            } else if (state.currentRemindersList.isEmpty()) {
               EmptyScreenBackground()
            } else {
                RemindersList(
                    reminders = state.currentRemindersList,
                    onDotsClicked = { shouldBlur = it },
                    onEdit = {
                        onEditReminderClicked(it.id)
                    },
                    onDelete = { event(MainScreenContract.Event.DeleteReminder(it)) },
                    onCancel = { event(MainScreenContract.Event.CancelReminder(it)) },
                    onDone = {
                        event(MainScreenContract.Event.CancelReminder(it))
                    },
                )
            }
        }
    }
}

@Composable
private fun EmptyScreenBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        //Arrow
        Row(Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(10f))
            Column(Modifier.fillMaxHeight()) {
                Image(painter = painterResource(id = R.drawable.arrow), contentDescription = null)
                Spacer(modifier = Modifier.weight(10f))
            }
            Spacer(modifier = Modifier.weight(2f))
        }

        // Bubbles right bottom
        Row(Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(10f))
            Column(Modifier.fillMaxHeight()) {
                Spacer(modifier = Modifier.weight(4f))
                Image(painter = painterResource(id = R.drawable.bubbles_right), contentDescription = null)
                Spacer(modifier = Modifier.weight(1f))
            }

        }

        //Bubbles left bottom
        Row(Modifier.fillMaxWidth()) {

            Column(Modifier.fillMaxHeight()) {
                Spacer(modifier = Modifier.weight(1f))
                Image(painter = painterResource(id = R.drawable.bubbles_left_bottom), contentDescription = null)
            }
            Spacer(modifier = Modifier.weight(1f))
        }

        // Bubbles left top
        Row(Modifier.fillMaxWidth()) {
            Column(Modifier.fillMaxHeight()) {
                Spacer(modifier = Modifier.weight(1f))
                Image(painter = painterResource(id = R.drawable.bubbles_left_top), contentDescription = null)
                Spacer(modifier = Modifier.weight(10f))
            }
            Spacer(modifier = Modifier.weight(1f))
        }


        // Central picture
        Row(Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1f))
            Column(modifier = Modifier
                .fillMaxHeight()
                ) {
                Spacer(modifier = Modifier.weight(3f))

                Image(
                    painter = painterResource(id = R.drawable.central_image),
                    contentDescription = null,
                    modifier = Modifier.size(300.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.weight(6f))
            }
            Spacer(modifier = Modifier.weight(1f))
        }

    }
}



