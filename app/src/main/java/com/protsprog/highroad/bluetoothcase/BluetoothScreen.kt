package com.protsprog.highroad.bluetoothcase

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.protsprog.highroad.BluetoothContainer
import com.protsprog.highroad.R
import com.protsprog.highroad.authentication.ui.components.LoadScreen
import com.protsprog.highroad.bluetoothcase.ui.components.ChatView
import com.protsprog.highroad.bluetoothcase.ui.components.DevicesListView
import com.protsprog.highroad.ui.components.AppBarMenu
import com.protsprog.highroad.ui.components.ItemMenu

@Composable
fun BluetoothScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {},
    bluetooth: BluetoothContainer,
    viewModel: BluetoothCaseViewModel = hiltViewModel()
) {
//    Inject
    bluetooth.service.also {
        it.injectViewModule(viewModel)
        it.checkStatus()
    }

    Scaffold(
        topBar = {
            AppBarMenu(
                title = "Bluetooth chat case",
                onBackPressed = {
                    bluetooth.service.closeThrowing()
                    onNavigateUp()
                },
                optionMenu = listOf(
                    ItemMenu(
                        label = "Show paired devices",
                        action = bluetooth.service::actionShowPairedDevices
                    ),
                    ItemMenu(
                        label = "Discover bluetooth devices",
                        action = bluetooth.service::actionDiscoverDevices
                    )
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
//        contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = dimensionResource(id = R.dimen.padding_medium),
                        horizontal = dimensionResource(id = R.dimen.padding_medium)
                    )
            ) {
                if (viewModel.uiState.supported) {
                    if (viewModel.uiState.enabled) {
//                    Bluetooth support
                        DevicesListView(
                            pairedList = viewModel.uiState.pairDevices,
                            foundList = viewModel.uiState.foundDevices,
                            onClickPaired = bluetooth.service::selectDevice,
                            onClickBonding = bluetooth.service::createBond
                        )
                        if (viewModel.uiState.showThrowing) {
//                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
                            ChatView(
                                onClickThrowMessage = bluetooth.service::onClickThrowMessage,
                                serverDeviceName = viewModel.uiState.deviceToThrow?.name ?: "",
                                serverDeviceAddress = viewModel.uiState.deviceToThrow?.address
                                    ?: "",
                                onClickCloseChat = bluetooth.service::closeThrowing,
                                messageToSend = viewModel.uiState.messageToSend,
                                changeMessageToSend = viewModel::changeMessageToSend,
                                listMessages = viewModel.uiState.chatMessages
                            )
                        }
                    } else {
                        Text(text = "You must Enable bluetooth")
                    }
                } else {
                    Text(text = "Bluetooth devices not support")
                }
            }

            if (viewModel.uiState.finding) {
                LoadScreen()
            }

            if (viewModel.uiState.showToast) {
                Toast.makeText(
                    LocalContext.current.applicationContext,
                    viewModel.uiState.errorMessage,
                    Toast.LENGTH_LONG
                ).show()
                viewModel.unsetShowToast()
            }
        }
    }
}