package com.apps.pushnotificationsapp.presentation.newReminderScreen.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
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
import com.apps.pushnotificationsapp.R
import com.apps.pushnotificationsapp.presentation.newReminderScreen.viewModel.NewReminderContract


@Composable
fun ReminderInputBox(
    state: NewReminderContract.State,
    event: (NewReminderContract.Event) -> Unit,
) {
    val alteHaasBoldFont = FontFamily(
        Font(R.font.alte_haas_grotesk_bold, FontWeight.W400),
    )

    Box(
        modifier = Modifier
            .padding(horizontal = 35.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(top = 15.dp, start = 5.dp, end = 5.dp)
                .shadow(
                    elevation = 25.dp,
                    shape = RoundedCornerShape(30.dp)
                )
                .background(Color.White)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                val alteHaasRegularFont = FontFamily(
                    Font(R.font.alte_haas_grotesk_regular, FontWeight.W400),
                )

                InputFormTextIcon("Title", R.drawable.ic_pen, 12.dp, alteHaasRegularFont)
                CustomTextField(
                    Modifier
                        .fillMaxWidth(),
                    state.currentTitle,
                    13.sp,
                    placeholder = "Insert Title",
                    KeyboardType.Text,
                    isError = false,
                    validate = {}
                ) { event(NewReminderContract.Event.SetCurrentTitle(it)) }


                InputDateSection(alteHaasRegularFont, state, event)

                InputFormTextIcon(
                    text = "Repeat",
                    iconId = R.drawable.ic_repeat,
                    iconSize = 14.dp,
                    fontFamily = alteHaasRegularFont
                )

                RepeatMenu() {
                    event(NewReminderContract.Event.SetCurrentRepeatMode(it))
                }
                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                        .fillMaxWidth()
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .clip(RoundedCornerShape(30.dp))
                .background(
                    colorResource(id = R.color.blue_7)
                )
        ) {
            Text(
                text = "Make your own Reminder.",
                fontFamily = alteHaasBoldFont,
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.padding(vertical = 5.dp, horizontal = 15.dp)
            )
        }

    }
}

@Composable
private fun InputFormTextIcon(
    text: String,
    iconId: Int,
    iconSize: Dp,
    fontFamily: FontFamily,
) {

    Row(modifier = Modifier.padding(top = 25.dp), verticalAlignment = Alignment.Top) {
        Text(
            text = text, fontFamily = fontFamily,
            fontSize = 18.sp,
            color = colorResource(id = R.color.dark_text),
        )
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .padding(start = 2.dp)
                .size(iconSize)
        )
    }
}

@Composable
private fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    fontSize: TextUnit,
    placeholder: String,
    keyboardType: KeyboardType,
    isError: Boolean,
    validate: () -> Unit,
    onValueChange: (String) -> Unit,
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Box(
            modifier = modifier.padding(top = 10.dp)
        ) {
            if (value.isEmpty() && !isFocused) {
                Text(
                    text = placeholder,
                    style = TextStyle(color = Color.Gray, fontSize = fontSize),
                    modifier = modifier.padding(start = 4.dp, end = 4.dp)
                )
            }

            BasicTextField(
                value = value,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                onValueChange = {
                    onValueChange(it)
                },
                textStyle = TextStyle(
                    color = if (isError) Color.Red else Color.Black.copy(alpha = 0.5f),
                    fontSize = fontSize
                ),
                modifier = modifier
                    .padding(start = 4.dp, end = 4.dp)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                        if (!focusState.isFocused) {
                            validate()

                        }
                    }
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(top = 3.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Black.copy(alpha = 0.5f))
        )
    }
}

@Composable
private fun InputDateSection(
    alteHaasRegularFont: FontFamily,
    state: NewReminderContract.State,
    event: (NewReminderContract.Event) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            InputFormTextIcon(
                text = "Time",
                iconId = R.drawable.ic_time,
                15.dp,
                alteHaasRegularFont
            )
            Row(verticalAlignment = Alignment.Bottom) {
                CustomTextField(
                    modifier = Modifier.width(IntrinsicSize.Min),
                    value = state.currentHour,
                    fontSize = 18.sp,
                    placeholder = "00",
                    keyboardType = KeyboardType.Number,
                    isError = state.isHourError,
                    validate = { event(NewReminderContract.Event.ValidateHour) }
                ) {
                    event(NewReminderContract.Event.SetCurrentHour(it))
                    Log.d("SET___", it)
                }
                Text(
                    text = ":",
                    fontFamily = alteHaasRegularFont,
                    color = Color.Black.copy(alpha = 0.5f),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
                CustomTextField(
                    modifier = Modifier.width(IntrinsicSize.Min),
                    value = state.currentMinute,
                    fontSize = 18.sp,
                    placeholder = "00",
                    keyboardType = KeyboardType.Number,
                    isError = state.isMinuteError,
                    validate = { event(NewReminderContract.Event.ValidateMinute) }
                ) { event(NewReminderContract.Event.SetCurrentMinute(it)) }


            }
        }

        Column {
            InputFormTextIcon(
                text = "Calendar",
                iconId = R.drawable.ic_calendar,
                16.dp,
                alteHaasRegularFont
            )
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.padding(top = 2.dp)
            ) {
                CustomTextField(
                    modifier = Modifier.width(38.dp),
                    value = state.currentDay,
                    fontSize = 16.sp,
                    placeholder = "dd",
                    keyboardType = KeyboardType.Number,
                    isError = state.isDayError,
                    validate = { event(NewReminderContract.Event.ValidateDay) }
                ) {
                    event(NewReminderContract.Event.SetCurrentDay(it))
                }
                Text(
                    text = ":",
                    fontFamily = alteHaasRegularFont,
                    color = Color.Black.copy(alpha = 0.5f),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
                CustomTextField(
                    modifier = Modifier.width(38.dp),
                    value = state.currentMonth,
                    fontSize = 16.sp,
                    placeholder = "mm",
                    keyboardType = KeyboardType.Number,
                    isError = state.isMonthError,
                    validate = { event(NewReminderContract.Event.ValidateMonth) }
                ) {
                    event(NewReminderContract.Event.SetCurrentMonth(it))
                }
                Text(
                    text = ":",
                    fontFamily = alteHaasRegularFont,
                    color = Color.Black.copy(alpha = 0.5f),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
                CustomTextField(
                    modifier = Modifier.width(50.dp),
                    value = state.currentYear,
                    fontSize = 16.sp,
                    placeholder = "yyyy",
                    keyboardType = KeyboardType.Number,
                    isError = state.isYearError,
                    validate = { event(NewReminderContract.Event.ValidateYear) }
                ) {
                    event(NewReminderContract.Event.SetCurrentYear(it))
                }

            }
        }
    }
}







