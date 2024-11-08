package com.apps.pushnotificationsapp.presentation.newReminderScreen

import android.preference.PreferenceActivity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.apps.pushnotificationsapp.R
import com.apps.pushnotificationsapp.presentation.mainScreen.LogoText
import com.apps.pushnotificationsapp.presentation.newReminderScreen.ui.Header
import com.apps.pushnotificationsapp.presentation.newReminderScreen.ui.ReminderInputBox
import com.apps.pushnotificationsapp.presentation.newReminderScreen.viewModel.NewReminderContract
import com.apps.pushnotificationsapp.presentation.newReminderScreen.viewModel.NewReminderViewModel
import com.apps.videolibrary.mvi.use

@Composable
fun NewReminderScreen(onCloseScreen: () -> Unit) {
    val viewModel = hiltViewModel<NewReminderViewModel>()
    val (state, event) = use(viewModel)
    val context = LocalContext.current
    LaunchedEffect(state.isDateError) {
        if (state.isDateError == true) {
            Toast.makeText(
                context,
                "Can not create reminder\n Select date which is after now",
                Toast.LENGTH_LONG
            ).show()
        }
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






