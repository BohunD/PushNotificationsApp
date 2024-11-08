package com.apps.pushnotificationsapp.presentation.newReminderScreen.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.apps.pushnotificationsapp.presentation.newReminderScreen.viewModel.NewReminderContract
import com.apps.pushnotificationsapp.presentation.mainScreen.ui.LogoText
import com.apps.pushnotificationsapp.R
@Composable
fun Header(
    onCloseScreen: () -> Unit,
    event: (NewReminderContract.Event) -> Unit,
    state: NewReminderContract.State,
) {
    Row(
        modifier = Modifier
            .padding(start = 40.dp, end = 40.dp, top = 27.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ReminderIcon(R.drawable.ic_cancel) {
            onCloseScreen()
        }
        LogoText(modifier = Modifier)
        LaunchedEffect(key1 = state.isSaved, key2 = state.isDateError) {
            if(state.isSaved==true){
                onCloseScreen()
            }
        }
        ReminderIcon(R.drawable.ic_okay) {
            event(NewReminderContract.Event.ValidateDate)

        }

    }
}

@Composable
fun ReminderIcon(iconRes: Int, onClick: () -> Unit) {
    Icon(
        painter = painterResource(id = iconRes),
        contentDescription = null,
        tint = Color.Unspecified,
        modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(100.dp))
            .clickable { onClick() }
    )
}
