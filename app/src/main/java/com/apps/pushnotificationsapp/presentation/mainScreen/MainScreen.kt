package com.apps.pushnotificationsapp.presentation.mainScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apps.pushnotificationsapp.R

@Composable
fun MainScreen(
    onNewReminderClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Header(onAddTaskClick = { onNewReminderClicked() })

        Image(
            painter = painterResource(id = R.drawable.empty_list_bg), contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 0.dp), contentScale = ContentScale.FillBounds
        )

    }
}

@Composable
private fun Header(onAddTaskClick: () -> Unit) {
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
        AddTaskButton(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 20.dp, top = 5.dp), coolveticaFont
        ) {
            onAddTaskClick()
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
