package com.apps.pushnotificationsapp.presentation.mainScreen.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.apps.pushnotificationsapp.R
import com.apps.pushnotificationsapp.domain.model.Reminder
import com.apps.pushnotificationsapp.presentation.mainScreen.viewmodel.MainScreenContract
import com.apps.pushnotificationsapp.presentation.mainScreen.viewmodel.MainScreenViewModel
import com.apps.videolibrary.mvi.use

@Composable
fun MainScreen(
    onNewReminderClicked: () -> Unit,
    onEditReminderClicked :(Int)->Unit
) {
    val viewModel = hiltViewModel<MainScreenViewModel>()
    val (state, event) = use(viewModel)
    LaunchedEffect(Unit) {
        event(MainScreenContract.Event.FetchRemindersFromDB)
    }
    var shouldBlur by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .then(if (shouldBlur) Modifier.blur(3.dp) else Modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Header(state.currentRemindersList.isEmpty(), onAddTaskClick = { onNewReminderClicked() })
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

            if (state.currentRemindersList.isEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.empty_list_bg),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 0.dp),
                    contentScale = ContentScale.FillBounds
                )
            } else {
                RemindersList(
                    reminders = state.currentRemindersList,
                    onDotsClicked = { shouldBlur = it },
                    onEdit = { onEditReminderClicked(it.id) },
                    onDelete = { event(MainScreenContract.Event.DeleteReminder(it)) }
                )
            }
        }

    }
}

@Composable
private fun RemindersList(
    reminders: List<Reminder>,
    onDotsClicked: (Boolean) -> Unit,
    onEdit: (Reminder) -> Unit,
    onDelete: (Reminder) -> Unit,
) {
    LazyColumn(
        Modifier
            .padding(top = 20.dp)
            .fillMaxSize()
            .background(Color.White)
    ) {
        items(reminders) { reminder ->
            ReminderListItem(
                reminder = reminder,
                onDotsClicked,
                onEdit = { onEdit(reminder) },
                onDelete = { onDelete(reminder) })
        }
    }
}

@Composable
private fun ReminderListItem(
    reminder: Reminder,
    onDotsClicked: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    var showActions by remember {
        mutableStateOf(false)
    }
    val coolveticaFont = FontFamily(
        Font(R.font.coolvetica_rg, FontWeight.W400),
    )
    val alteHaasGroteskFont = FontFamily(
        Font(R.font.alte_haas_grotesk_regular, FontWeight.W400),
    )
    LaunchedEffect(showActions) {
        onDotsClicked(showActions)
    }
    Box(
        modifier = Modifier
            .padding(top = 10.dp, start = 5.dp, end = 5.dp, bottom = 10.dp)
            .shadow(
                elevation = 15.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = colorResource(id = R.color.blue_5)
            )
            .background(colorResource(id = R.color.blue_9))
            .fillMaxWidth()
    )
    {
        Row(
            modifier = Modifier
                .padding(start = 0.6.dp, top = 0.6.dp, end = 0.3.dp)
                .clip(RoundedCornerShape(19.4.dp))
                .background(Color.White)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.padding(start = 15.dp, bottom = 15.dp, top = 15.dp)) {
                Text(
                    text = reminder.title,
                    fontFamily = coolveticaFont,
                    fontWeight = FontWeight.W400,
                    fontSize = 20.sp,
                    color = colorResource(id = R.color.black_text),

                    )
                Text(
                    text = "|${formatDate(reminder.date)}",
                    fontFamily = alteHaasGroteskFont,
                    fontWeight = FontWeight.W400,
                    fontSize = 10.sp,
                    color = Color.Black.copy(alpha = 0.5f),
                    modifier = Modifier.padding(top = 4.dp)
                )


                Icon(
                    painter = painterResource(id = R.drawable.ic_dots), contentDescription = null,
                    tint = Color.Unspecified, modifier = Modifier
                        .padding(top = 25.dp)
                        .width(30.dp)
                        .clickable { showActions = !showActions }
                        .popUpBox(
                            enabled = showActions,
                            onDismiss = { showActions = !showActions },
                            onEdit = {
                                onEdit()
                                onDotsClicked(false)
                            },
                            onDelete = {
                                onDelete()
                                onDotsClicked(false)
                            }
                        )
                )
            }

            Column(modifier = Modifier.padding(end = 25.dp, bottom = 15.dp, top = 12.dp)) {
                Text(
                    text = formatTime(reminder.time),
                    fontFamily = coolveticaFont,
                    fontWeight = FontWeight.W400,
                    fontSize = 26.sp,
                    color = colorResource(id = R.color.black_text),
                )
                Row(modifier = Modifier.padding(top = 10.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_okay),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(35.dp)
                            .clip(RoundedCornerShape(100.dp))
                            .clickable { }
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cancel),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(35.dp)
                            .clip(RoundedCornerShape(100.dp))
                            .clickable { }

                    )
                }

            }

        }
    }
}

@Composable
private fun Header(isListEmpty: Boolean, onAddTaskClick: () -> Unit) {
    val coolveticaFont = FontFamily(
        Font(R.font.coolvetica_rg, FontWeight.W400),
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        LogoText(
            Modifier
                .padding(vertical = 17.dp)
                .align(Alignment.Center)
        )
        if (isListEmpty) {
            AddTaskButton(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 20.dp, top = 5.dp), coolveticaFont
            ) {
                onAddTaskClick()
            }
        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_add_task),
                contentDescription = null, tint = Color.Unspecified,
                modifier = Modifier
                    .padding(end = 40.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .size(34.dp)
                    .align(Alignment.CenterEnd)
                    .clickable {
                        onAddTaskClick()
                    }
            )
        }
    }
}

@Composable
fun LogoText(modifier: Modifier) {
    val coolveticaCondensedFont = FontFamily(
        Font(R.font.coolvetica_condensed_rg, FontWeight.W400),
    )
    Text(
        text = "TestReminder",
        fontSize = 28.sp,
        fontFamily = coolveticaCondensedFont,
        modifier = modifier
    )
}

@Composable
private fun AddTaskButton(modifier: Modifier, coolveticaFont: FontFamily, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .padding(start = 26.dp)
                .width(74.dp)
                .height(18.dp)
                .clip(RoundedCornerShape(topEnd = 34.dp, bottomEnd = 34.dp))
                .background(colorResource(id = R.color.blue_5)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(72.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(topEnd = 34.dp, bottomEnd = 34.dp))
                    .background(colorResource(id = R.color.add_task_fill)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Add Task",
                    fontWeight = FontWeight.W400,
                    fontSize = 10.sp,
                    lineHeight = 10.sp,
                    color = colorResource(id = R.color.blue_9),
                    fontFamily = coolveticaFont
                )
            }
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_add_task),
            contentDescription = null, tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(34.dp)
        )

    }
}

fun formatDate(date: String): String {
    val parts = date.split("/")
    if (parts.size != 3) return date

    val day = String.format("%02d", parts[0].toInt())
    val month = String.format("%02d", parts[1].toInt())
    val year = parts[2]

    return "$day/$month/$year"
}

fun formatTime(time: String): String {
    try {
        val parts = time.split(":")
        if (parts.size != 2) return time
        val hour = String.format("%02d", parts[0].toInt())
        val minute = String.format("%02d", parts[1].toInt())

        return "$hour:$minute"
    } catch (e: Exception) {
        Log.e("Error formatTime:", e.message.toString())
        return ""
    }
}
