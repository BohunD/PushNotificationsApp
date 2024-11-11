package com.apps.pushnotificationsapp.presentation.mainScreen.ui.internalViews


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apps.pushnotificationsapp.R
import com.apps.pushnotificationsapp.presentation.util.Typography.coolveticaCondensedFont
import com.apps.pushnotificationsapp.presentation.util.Typography.coolveticaFont

@Composable
fun Header(isListEmpty: Boolean, onAddTaskClick: () -> Unit) {
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
        Row(Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(16f))
            AddTaskButton(
                modifier = Modifier
                    .padding( top = 20.dp, end = 5.dp),
                isListEmpty
            ) {
                onAddTaskClick()
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun LogoText(modifier: Modifier) {
    Text(
        text = stringResource(R.string.testreminder),
        fontSize = 28.sp,
        fontFamily = coolveticaCondensedFont,
        modifier = modifier
    )
}

@Composable
private fun AddTaskButton(modifier: Modifier, isListEmpty: Boolean, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        if(isListEmpty) {
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
                        text = stringResource(R.string.add_task),
                        fontWeight = FontWeight.W400,
                        fontSize = 10.sp,
                        lineHeight = 10.sp,
                        color = colorResource(id = R.color.blue_9),
                        fontFamily = coolveticaFont
                    )
                }
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

