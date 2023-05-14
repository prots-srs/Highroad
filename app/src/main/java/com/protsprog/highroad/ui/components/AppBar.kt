package com.protsprog.highroad.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import com.protsprog.highroad.R
import com.protsprog.highroad.entrance.ui.theme.EntranceTheme
import com.protsprog.highroad.nav.Compose
import com.protsprog.highroad.nav.Entrance
import com.protsprog.highroad.nav.HighroadDestination

@Composable
fun AppBar(
    title: String,
    currentScreen: HighroadDestination,
//    currentScreen: NavDestination?,
    modifier: Modifier = Modifier,
    onBackPressed: (HighroadDestination) -> Unit
) {
    if (currentScreen != Entrance) {
        TopAppBar(
            modifier = modifier,
            backgroundColor = MaterialTheme.colorScheme.primary,
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            navigationIcon = {
                FilledIconButton(
                    onClick = { onBackPressed(currentScreen) },
                    modifier = Modifier.padding(8.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.theming_back_button),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppBarPreview() {
    AppBar(
        title = "Compose case",
        currentScreen = Compose,
        onBackPressed = {}
    )
}

@Preview(showBackground = true)
@Composable
fun AppBarPreviewThemes() {
    EntranceTheme {
        AppBar(
            title = "Compose case",
            currentScreen = Compose,
            onBackPressed = {}
        )
    }
}