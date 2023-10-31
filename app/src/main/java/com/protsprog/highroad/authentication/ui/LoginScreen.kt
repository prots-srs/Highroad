package com.protsprog.highroad.authentication.ui

/*
TO READ
https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#Button(kotlin.Function0,androidx.compose.ui.Modifier,kotlin.Boolean,androidx.compose.ui.graphics.Shape,androidx.compose.material3.ButtonColors,androidx.compose.material3.ButtonElevation,androidx.compose.foundation.BorderStroke,androidx.compose.foundation.layout.PaddingValues,androidx.compose.foundation.interaction.MutableInteractionSource,kotlin.Function1)
https://developer.android.com/reference/kotlin/androidx/compose/animation/package-summary#fadeIn(androidx.compose.animation.core.FiniteAnimationSpec,kotlin.Float)

https://developer.android.com/training/sign-in/credential-provider

https://developer.android.com/codelabs/biometric-login?hl=en#0

https://developer.android.com/jetpack/compose/tooling
https://developer.android.com/jetpack/compose/animation/composables-modifiers#animatedvisibility
 */
import androidx.biometric.BiometricManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.R
import com.protsprog.highroad.authentication.ui.components.AuthFormButtonEscape
import com.protsprog.highroad.authentication.ui.components.AuthFormButtonSubmit
import com.protsprog.highroad.authentication.ui.components.LoadScreen
import com.protsprog.highroad.authentication.ui.components.TouchBiometric

private val layoutStep = 8.dp

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    auth: AuthUIState = AuthUIState(),
    login: LoginState = LoginState(),
    onChangeEmail: (String) -> Unit = {},
    onChangePassword: (String) -> Unit = {},
    onSubmit: () -> Unit = {},
    onNavigateUp: () -> Unit = {},
    clearForm: () -> Unit = {},
    biometricStrings: BiometricManager.Strings? = null,
    showBiometricButton: Boolean = false,
    onClickBiometric: () -> Unit = {},
) {
    val isError = auth.errorLogin.isNotEmpty()

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = modifier.width(300.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthFormTitle()
            Spacer(modifier.height(layoutStep * 2))
            AuthFormFieldEmail(
                modifier = modifier,
                value = login.email,
                onValueChange = onChangeEmail,
                isError = isError
            )
            Spacer(modifier.height(layoutStep * 2))
            AuthFormFieldPassword(
                modifier = modifier,
                value = login.password,
                onValueChange = onChangePassword,
                onClickLogin = onSubmit,
                isError = isError,
            )
            AnimatedVisibility(isError) {
                ErrorForInput(text = auth.errorLogin)
            }
            Spacer(modifier.height(layoutStep * 3))
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AuthFormButtonEscape(
                    modifier = modifier,
                    onClick = {
                        clearForm()
                        onNavigateUp()
                    }
                )
                AuthFormButtonSubmit(
                    modifier = modifier,
                    onClick = onSubmit
                )
            }
            if (showBiometricButton) {
                Spacer(modifier.height(layoutStep * 3))
                TouchBiometric(
                    biometricStrings = biometricStrings,
                    onClickShowBiometric = onClickBiometric
                )
            }
        }
    }
    AnimatedVisibility(
        visible = auth.sendRequest,
        enter = fadeIn(
            initialAlpha = 0.4f
        ),
        exit = fadeOut(
            animationSpec = tween(durationMillis = 250)
        )
    ) {
        LoadScreen(modifier = modifier)
    }
}


//@Preview(device = "id:pixel_5", showBackground = true, backgroundColor = 0xFFF0F0FF)
//@Composable
//fun LoginScreenPreview() {
//    AuthTheme {
//        LoginScreen(
//            loginError = "Test error string",
//        )
//    }
//}

@Composable
fun AuthFormTitle() {
    Text(
        text = "Sign in",
        style = MaterialTheme.typography.titleLarge
    )
}

/*
@Preview
@Composable
fun AuthFormTitlePreview() {
    AuthTheme {
        AuthFormTitle()
    }
}
*/

@Composable
fun ErrorForInput(
    modifier: Modifier = Modifier,
    text: String = ""
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = layoutStep),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.error
        )
    }
}

/*
@Preview(showBackground = true, backgroundColor = 0xFFF0F0F0)
@Composable
fun ErrorForInputPreview() {
    AuthTheme {
        ErrorForInput(text = "kfrofjro vrivnri rvinrinv")
    }
}
*/


@Composable
fun AuthFormFieldEmail(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    isError: Boolean = false
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        label = { Text("Email") },
        singleLine = true,
        onValueChange = onValueChange,
        isError = isError
    )
}

/*
@Preview
@Composable
fun AuthFormFieldEmailPreview() {
    AuthTheme {
        AuthFormFieldEmail()
    }
}
*/

@Composable
fun AuthFormFieldPassword(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    onClickLogin: () -> Unit = {},
    isError: Boolean = false
) {
    var passwordHidden by remember { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        label = { Text("Password") },
        singleLine = true,
        onValueChange = onValueChange,
        visualTransformation = if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            IconButton(onClick = { passwordHidden = !passwordHidden }) {
                val visibilityIcon =
                    if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description =
                    if (passwordHidden) stringResource(R.string.auth_show_password) else stringResource(
                        R.string.auth_hide_password
                    )
                Icon(imageVector = visibilityIcon, contentDescription = description)
            }
        },
        isError = isError,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
        keyboardActions = KeyboardActions(
            onGo = {
                focusManager.clearFocus()
                onClickLogin()
            }
        )
    )
}

/*
@Preview
@Composable
fun AuthFormFieldPasswordPreview() {
    AuthTheme {
        AuthFormFieldPassword()
    }
}
*/


