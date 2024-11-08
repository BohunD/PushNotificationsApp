package com.apps.pushnotificationsapp.presentation.newReminderScreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.apps.pushnotificationsapp.R

@Composable
fun RepeatMenu(
    options: List<String> = listOf("Once", "Daily", "Mon to Fri"),
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    var rowSize by remember { mutableStateOf(Size.Zero) }


    val shapes = Shapes(
        small = RoundedCornerShape(30.dp),
        medium = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp),
        large = RoundedCornerShape(30.dp)
    )
    Box(modifier = Modifier
        .padding(top = 10.dp)
        .fillMaxWidth()
        .onGloballyPositioned { layoutCoordinates ->
            rowSize = layoutCoordinates.size.toSize()
        }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    colorResource(id = R.color.gray_bg), shape =
                    if (!expanded) RoundedCornerShape(30.dp) else RoundedCornerShape(
                        topEnd = 25.dp,
                        topStart = 25.dp
                    )
                )
                .clickable { expanded = !expanded }
                .padding(horizontal = 15.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = selectedOption, fontSize = 14.sp, color = Color.Black.copy(alpha = 0.5f))
            Icon(painter = painterResource(id = R.drawable.ic_expand), contentDescription = null, tint = Color.Unspecified, modifier = Modifier.padding(end = 5.dp).size(16.dp))
        }


        DropdownMenu(
            expanded = expanded,
            shape = shapes.medium,
            shadowElevation = 0.dp,
            containerColor = colorResource(id = R.color.gray_bg),
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { rowSize.width.toDp() })

        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .height(27.dp)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(start = 15.dp, top = 2.dp),
                    text = {
                        Text(
                            text = option,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W400,
                            color = Color.Black.copy(alpha = 0.5f)
                        )
                    },
                    onClick = {
                        expanded = false
                        onOptionSelected(option)
                    }
                )
            }

        }
    }
}
