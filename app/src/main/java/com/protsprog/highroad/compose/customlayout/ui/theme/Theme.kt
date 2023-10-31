package com.protsprog.highroad.compose.customlayout.ui.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Yellow,
    secondary = MintGreen,
    tertiary = Coral,
    secondaryContainer = Yellow,
    surface = White
)
private val shapes: Shapes
    @Composable
    get() = MaterialTheme.shapes.copy(
        large = CircleShape
    )
@Composable
fun JetLaggedTheme(
    content: @Composable () -> Unit,
) {

    val view = LocalView.current
    val context = LocalContext.current
    if (!view.isInEditMode) {
        SideEffect {
            WindowCompat.getInsetsController(context.findActivity().window, view)
                .isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        shapes = shapes,
        content = content
    )
}
private tailrec fun Context.findActivity(): Activity =
    when (this) {
        is Activity -> this
        is ContextWrapper -> this.baseContext.findActivity()
        else -> throw IllegalArgumentException("Could not find activity!")
    }