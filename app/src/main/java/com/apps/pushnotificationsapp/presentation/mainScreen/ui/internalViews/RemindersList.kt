package com.apps.pushnotificationsapp.presentation.mainScreen.ui.internalViews

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apps.pushnotificationsapp.R
import com.apps.pushnotificationsapp.domain.model.Reminder
import com.apps.pushnotificationsapp.presentation.util.Typography.alteHaasGroteskFont
import com.apps.pushnotificationsapp.presentation.util.Typography.coolveticaFont

@Composable
fun RemindersList(
    reminders: List<Reminder>,
    onDotsClicked: (Boolean) -> Unit,
    onEdit: (Reminder) -> Unit,
    onDelete: (Reminder) -> Unit,
    onCancel: (Reminder) -> Unit,
    onDone: (Reminder) -> Unit,
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
                onDelete = { onDelete(reminder) },
                onCancel = { onCancel(reminder) },
                onDone = { onDone(reminder) },
            )
        }
    }
}

@Composable
private fun ReminderListItem(
    reminder: Reminder,
    onDotsClicked: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onCancel: () -> Unit,
    onDone: () -> Unit,
) {
    var showActions by remember {
        mutableStateOf(false)
    }

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
            Column(modifier = Modifier.padding(start = 15.dp, bottom = 15.dp, top = 15.dp).fillMaxWidth(0.6f)) {
                Text(
                    text = reminder.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = coolveticaFont,
                    fontWeight = FontWeight.W400,
                    fontSize = 20.sp,
                    color = colorResource(id = R.color.black_text),
                    )
                Text(
                    text = reminder.date,
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
                    text = reminder.time,
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
                                then (if (!reminder.isCancelledToday) {
                            Modifier.clickable { onDone() }
                        } else Modifier.alpha(0.5f))
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cancel),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(35.dp)
                            .clip(RoundedCornerShape(100.dp))
                                then (if (!reminder.isCancelledToday) {
                            Modifier.clickable { onCancel() }
                        } else Modifier.alpha(0.5f))

                    )
                }
            }


        }
    }
}
