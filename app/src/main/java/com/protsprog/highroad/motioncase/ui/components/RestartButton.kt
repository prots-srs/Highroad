package com.protsprog.highroad.motioncase.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.flightsearch.ui.theme.FlightSearchTheme

private val stepLayout = 12.dp

@Composable
fun RestartButton(
    onRestartGame: () -> Unit = {}
) {
    Button(
        onClick = onRestartGame,
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(vertical = stepLayout, horizontal = stepLayout * 2)
    ) {
        Text(
            text = "Restart game".uppercase(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview(
    showBackground = true,
    device = "id:pixel_4"
)
@Composable
fun RestartButtonPreview() {
    FlightSearchTheme {
        RestartButton()
    }
}