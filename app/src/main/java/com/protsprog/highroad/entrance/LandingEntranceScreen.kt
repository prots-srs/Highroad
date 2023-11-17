package com.protsprog.highroad.entrance

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.protsprog.highroad.R
import com.protsprog.highroad.entrance.ui.theme.EntranceTheme
import kotlinx.coroutines.delay

private const val SplashWaitTime: Long = 200

@Composable
fun LandingEntranceScreen(
    onTimeout: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiaryContainer),
        contentAlignment = Alignment.Center
    ) {
        // LaunchedEffect and rememberUpdatedState step
        // Make LandingScreen disappear after loading data
        val currentOnTimeout by rememberUpdatedState(onTimeout)
        LaunchedEffect(Unit) {
            delay(SplashWaitTime)
            currentOnTimeout()
        }

        Image(painterResource(id = R.drawable.splash_icon), contentDescription = null)
    }
}

@Preview(showBackground = true)
@Composable
fun LandingEntranceScreenPreview() {
    EntranceTheme {
        LandingEntranceScreen()
    }
}