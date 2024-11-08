package com.apps.pushnotificationsapp.presentation.mainScreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.apps.pushnotificationsapp.R
import kotlin.math.min

@Composable
fun Modifier.popUpBox(
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    enabled: Boolean = true,
    onDismiss: () -> Unit = {},
    onEdit: () -> Unit,
    onDelete: () -> Unit

): Modifier {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenWidthPx = remember { with(density) { configuration.screenWidthDp.dp.roundToPx() } }
    val screenHeightPx = remember { with(density) { configuration.screenHeightDp.dp.roundToPx() } }

    var positionInRoot by remember { mutableStateOf(IntOffset.Zero) }
    var tooltipSize by remember { mutableStateOf(IntSize(0, 0)) }
    var componentSize by remember { mutableStateOf(IntSize(0, 0)) }
    val tooltipBottomPadding = remember { with(density) { 25.dp.roundToPx() } }
    val tooltipOffset by remember(positionInRoot, componentSize, tooltipSize) {
        derivedStateOf {
            calculateOffset(
                positionInRoot,
                componentSize,
                tooltipSize,
                screenWidthPx,
                screenHeightPx,
                horizontalAlignment,
                verticalAlignment,
                tooltipBottomPadding
            )
        }
    }

    if (enabled) {
        Popup(
            alignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clickable(
                        onClick = {
                            onDismiss()
                        }
                    ),
            ) {
                ChooseActionBox(
                    modifier = Modifier
                        .offset { tooltipOffset }
                        .onSizeChanged { tooltipSize = it },
                    onDismiss,
                    onEdit, onDelete
                )

            }
        }
    }

    return this then Modifier.onPlaced {
        componentSize = it.size
        positionInRoot = IntOffset(it.positionInRoot().x.toInt(), it.positionInRoot().y.toInt())
    }
}

private fun calculateOffset(
    positionInRoot: IntOffset,
    componentSize: IntSize,
    tooltipSize: IntSize,
    screenWidthPx: Int,
    screenHeightPx: Int,
    horizontalAlignment: Alignment.Horizontal,
    verticalAlignment: Alignment.Vertical,
    bottomPadding: Int,
): IntOffset {
    val horizontalAlignmentPosition = when (horizontalAlignment) {
        Alignment.Start -> positionInRoot.x
        Alignment.End -> positionInRoot.x + componentSize.width - tooltipSize.width
        else -> positionInRoot.x + (componentSize.width / 2) - (tooltipSize.width / 2)
    }
    val verticalAlignmentPosition = when (verticalAlignment) {
        Alignment.Top -> positionInRoot.y - tooltipSize.height
        Alignment.Bottom -> positionInRoot.y + componentSize.height
        else -> positionInRoot.y + (componentSize.height) - bottomPadding
    }
    return IntOffset(
        x = min(screenWidthPx - tooltipSize.width, horizontalAlignmentPosition),
        y = min(screenHeightPx - tooltipSize.height, verticalAlignmentPosition),
    )
}


@Composable
private fun ChooseActionBox(
    modifier: Modifier,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    val alteHaasGroteskFont = FontFamily(
        Font(R.font.alte_haas_grotesk_regular, FontWeight.W400),
    )
    Column(
        modifier = modifier
            .padding(start = 55.dp, top = 20.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .width(IntrinsicSize.Min)
    ) {
        Text(
            "Edit...",
            fontFamily = alteHaasGroteskFont,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Black, RoundedCornerShape(topEnd = 15.dp, topStart = 15.dp))
                .clickable {
                    onDismiss()
                    onEdit()
                }
                .padding(10.dp)
                .padding(end = 25.dp)

        )
        Text(
            "Delete...",
            fontFamily = alteHaasGroteskFont,
            color = colorResource(id = R.color.red_text),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    Color.Black,
                    RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)
                )
                .clickable {
                    onDismiss()
                    onDelete()
                }
                .padding(10.dp)
                .padding(end = 25.dp)

        )
    }
}