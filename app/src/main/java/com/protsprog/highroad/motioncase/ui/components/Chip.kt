package com.protsprog.highroad.motioncase.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.flightsearch.ui.theme.FlightSearchTheme

@Composable
fun Chip(
    modifier: Modifier = Modifier,
    currentChip: Int,
    face: Int,
    onChangeOffset: (Int, Offset) -> Unit,
    offset: IntOffset
) {
    Box(
        modifier = modifier
            .size(CHIP_SIZE)
            .offset {
                offset
            }
            .pointerInput(face) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    onChangeOffset(face, dragAmount)
                }
            }
    ) {
        ChipPeace(
            modifier = modifier,
            isMoving = currentChip == face,
            face = face
        )
    }
}

@Composable
fun ChipPeace(
    modifier: Modifier = Modifier,
    isMoving: Boolean = false,
    face: Int
) {
    val backgroundColorChip =
        if (isMoving) MaterialTheme.colorScheme.secondaryContainer else Color.White
    val colorChip =
        if (isMoving) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.primary

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 1.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(color = backgroundColorChip)
            .border(
                width = 3.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            )
    ) {
        Row(
            modifier = modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = face.toString(),
                style = MaterialTheme.typography.headlineLarge,
                color = colorChip,
                lineHeight = MaterialTheme.typography.labelSmall.lineHeight
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFF0F00F,
    widthDp = 85,
    heightDp = 85
)
@Composable
fun ChipPreview() {
    FlightSearchTheme {
        ChipPeace(
            face = 5,
            isMoving = true,
        )
    }
}