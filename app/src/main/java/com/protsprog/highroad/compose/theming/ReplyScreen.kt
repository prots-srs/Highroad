package com.protsprog.highroad.compose.theming

/*
TO READ
https://codelabs.developers.google.com/jetpack-compose-adaptability
 */
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import com.protsprog.highroad.compose.theming.data.local.LocalEmailsDataProvider
import com.protsprog.highroad.compose.theming.ui.ReplyApp
import com.protsprog.highroad.compose.theming.ui.theme.ReplyTheme
import com.protsprog.highroad.util.DevicePosture
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ReplyScreen(
    windowWidthClass: WindowWidthSizeClass,
    foldingDevicePosture: DevicePosture,
    viewModel: ReplyHomeViewModel = viewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    ReplyApp(
        replyHomeUIState = uiState,
        foldingDevicePosture = foldingDevicePosture,
        windowSize = windowWidthClass
    )
}

@Preview(showBackground = true)
@Composable
fun ReplyAppPreview() {
    ReplyTheme {
        ReplyApp(
            replyHomeUIState = ReplyHomeUIState(emails = LocalEmailsDataProvider.allEmails),
            windowSize = WindowWidthSizeClass.Compact,
            foldingDevicePosture = DevicePosture.NormalPosture,
        )
    }
}

@Preview(showBackground = true, widthDp = 700)
@Composable
fun ReplyAppPreviewTablet() {
    ReplyTheme {
        ReplyApp(
            replyHomeUIState = ReplyHomeUIState(emails = LocalEmailsDataProvider.allEmails),
            windowSize = WindowWidthSizeClass.Medium,
            foldingDevicePosture = DevicePosture.NormalPosture,
        )
    }
}

@Preview(showBackground = true, widthDp = 1000)
@Composable
fun ReplyAppPreviewDesktop() {
    ReplyTheme {
        ReplyApp(
            replyHomeUIState = ReplyHomeUIState(emails = LocalEmailsDataProvider.allEmails),
            windowSize = WindowWidthSizeClass.Expanded,
            foldingDevicePosture = DevicePosture.NormalPosture,
        )
    }
}