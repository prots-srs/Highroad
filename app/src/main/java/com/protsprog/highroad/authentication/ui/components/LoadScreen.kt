package com.protsprog.highroad.authentication.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.authentication.ui.theme.AuthTheme

private val layoutStep = 8.dp

@Composable
fun LoadScreen(
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                0.7f at 500
            },
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = modifier
            .alpha(0.5f)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = modifier
                .size(layoutStep * 8)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = alpha))
        )
    }
}

@Preview(device = "id:pixel_5", showBackground = true, backgroundColor = 0xFFF0F0FF)
@Composable
fun LoadScreenPreview() {
    AuthTheme {
        LoadScreen()
    }
}