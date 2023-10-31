package com.protsprog.highroad.authentication.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.R

private val layoutStep = 8.dp

@Composable
fun AuthFormButtonEscape(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    OutlinedButton(
        modifier = modifier
            .height(layoutStep * 6),
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = layoutStep * 5)
    ) {
        Text(stringResource(R.string.auth_escape))
    }
}

/*
@Preview
@Composable
fun AuthFormButtonEscapePreview() {
    AuthTheme {
        AuthFormButtonEscape()
    }
}
*/
@Composable
fun AuthFormButtonSubmit(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Button(
        modifier = modifier
            .height(layoutStep * 6),
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = layoutStep * 5)
    ) {
        Text(
            text = stringResource(R.string.auth_sign_in),
            maxLines = 1,
            minLines = 1
        )
    }
}

/*
@Preview
@Composable
fun AuthFormButtonSubmitPreview() {
    AuthTheme {
        AuthFormButtonSubmit()
    }
}*/
