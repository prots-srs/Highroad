package com.protsprog.highroad.motioncase.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.flightsearch.ui.theme.FlightSearchTheme
import kotlin.math.roundToInt

val CHIP_SIZE: Dp = 85.dp
private val BOARD_PADDING: Dp = 5.dp

@Composable
fun GameBoard(
    modifier: Modifier = Modifier,
    chips: Map<Int, IntOffset>,
    currentChip: Int,
    onChangeOffset: (Int, Offset) -> Unit = { _, _ -> }
) {
    Box(
        modifier = modifier
            .size(CHIP_SIZE * 4 + BOARD_PADDING * 2)
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.secondary)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.small
            )
            .padding(all = BOARD_PADDING)
    ) {
        chips.forEach { chipData ->
            if (chipData.key > 0) {
                Chip(
                    face = chipData.key,
                    offset = chipData.value,
                    onChangeOffset = onChangeOffset,
                    currentChip = currentChip,
                    modifier = modifier
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF0F5FF5,
    device = "id:pixel_4"
)
@Composable
fun GameBoardPreview() {
    FlightSearchTheme {
        GameBoard(
            chips = chipsDemo,
            currentChip = 3
        )
    }
}

private val demosize: Int = 235
val chipsDemo = mapOf(
    1 to IntOffset(0, 0),
    2 to IntOffset(demosize, 0),
    3 to IntOffset(demosize * 2, 0),
    4 to IntOffset(0, demosize),
    5 to IntOffset(demosize, demosize),
    6 to IntOffset(demosize * 2, demosize),
    7 to IntOffset(0, demosize * 2),
    8 to IntOffset(demosize, demosize * 2),
    0 to IntOffset(demosize * 2, demosize * 2),
)