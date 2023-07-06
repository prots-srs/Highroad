package com.protsprog.highroad.articles.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.articles.ui.theme.ArticlesTheme

@Preview(showBackground = true)
@Composable
fun PreviewNetworkErrorIndicator() {
    ArticlesTheme {
        NetworkErrorIndicator(space = 16.dp)
    }
}

@Composable
fun NetworkErrorIndicator(
    space: Dp,
    visibility: Boolean = true,
    onClick: () -> Unit = {}
) {
    if (visibility) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = space, horizontal = 0.dp)
                .clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.CloudOff,
                contentDescription = null,
                tint = Color.LightGray
            )
            Spacer(modifier = Modifier.width(space))
            Text(
                "Network error",
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray
            )
        }
    }
}