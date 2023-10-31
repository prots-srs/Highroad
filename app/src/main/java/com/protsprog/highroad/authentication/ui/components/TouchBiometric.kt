package com.protsprog.highroad.authentication.ui.components

import android.util.Log
import androidx.biometric.BiometricManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.R
import com.protsprog.highroad.authentication.ui.theme.AuthTheme

private val layoutStep = 8.dp

@Composable
fun TouchBiometric(
    modifier: Modifier = Modifier,
    biometricStrings: BiometricManager.Strings? = null,
    onClickShowBiometric: () ->Unit = {}
) {

//    Log.d("TEST_BIOMETRIC", "promptMessage: ${biometricStrings?.buttonLabel}")
//    Log.d("TEST_BIOMETRIC", "promptMessage: ${biometricStrings?.promptMessage}")
//    Log.d("TEST_BIOMETRIC", "settingName: ${biometricStrings?.settingName}")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClickShowBiometric()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.Fingerprint,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = modifier.size(48.dp)
        )
        Spacer(modifier.height(layoutStep * 2))
        val title = biometricStrings?.let {
            it.buttonLabel.toString()
        } ?: "Use biometric"
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.tertiary
        )

    }
}

@Preview(device = "id:pixel_5", showBackground = true, backgroundColor = 0xFFE7E4F1)
@Composable
fun TouchBiometricPreview() {
    AuthTheme {
        TouchBiometric()
    }
}