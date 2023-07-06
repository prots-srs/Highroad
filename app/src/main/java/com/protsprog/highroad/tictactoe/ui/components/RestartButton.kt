package com.protsprog.highroad.tictactoe.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.R
import com.protsprog.highroad.tictactoe.ui.theme.TicTacToeTheme

@Preview(showBackground = true)
@Composable
fun PreviewRestartButton() {
    TicTacToeTheme {
        RestartButton()
    }
}

@Composable
fun RestartButton(
    modifier: Modifier = Modifier,
    onClickRestart: () -> Unit = {}
) {
    FilledIconButton(
        onClick = onClickRestart,
        modifier = modifier.size(32.dp, 32.dp),
//        colors = IconButtonDefaults.filledIconButtonColors(
//            containerColor = MaterialTheme.colorScheme.primary,
//            contentColor = MaterialTheme.colorScheme.onPrimary
//        )
    ) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = stringResource(R.string.animating_refresh),
            modifier = modifier.size(24.dp)
        )
    }
}