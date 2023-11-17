package com.protsprog.highroad.ui.components

/*
TO READ
https://m3.material.io/components/top-app-bar/specs
 */
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.R
import com.protsprog.highroad.entrance.ui.theme.EntranceTheme
import com.protsprog.highroad.nav.Compose

@Composable
fun AppBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        backgroundColor = MaterialTheme.colorScheme.surface,
        title = { AppBarTitle(title) },
        navigationIcon = { AppBarBackIcon(onBackPressed) },
    )
}

@Preview(showBackground = true)
@Composable
fun AppBarPreview() {
    AppBar(
        title = Compose.title,
        onBackPressed = {}
    )
}

@Preview(showBackground = true)
@Composable
fun AppBarPreviewThemes() {
    EntranceTheme {
        AppBar(
            title = Compose.title,
            onBackPressed = {}
        )
    }
}

/*
@Preview(showBackground = true)
@Composable
fun AppBarPreviewThemeTicTacToe() {
    TicTacToeTheme {
        AppBar(
            title = Compose.title,
            onBackPressed = {}
        )
    }
}

 */