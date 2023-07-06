package com.protsprog.highroad.compose.sideeffects

/*
READ
https://developer.android.com/codelabs/jetpack-compose-advanced-state-side-effects
 */
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.protsprog.highroad.compose.sideeffects.details.DetailsScreen

@Composable
fun MainScreen(
    onExploreItemClicked: OnExploreItemClicked = {},
) {
    Surface(color = MaterialTheme.colors.primary) {
        var showLandingScreen by remember { mutableStateOf(true) }
        if (showLandingScreen) {
            LandingScreen(onTimeout = { showLandingScreen = false })
        } else {
            CraneHome(onExploreItemClicked = onExploreItemClicked)
        }
    }
}