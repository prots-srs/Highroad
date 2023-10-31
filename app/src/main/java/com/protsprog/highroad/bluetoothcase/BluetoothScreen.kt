package com.protsprog.highroad.bluetoothcase

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.protsprog.highroad.BluetoothContainer
import com.protsprog.highroad.R
import com.protsprog.highroad.TAG_BLUETOOTH_TEST
import com.protsprog.highroad.nav.BluetoothCase
import com.protsprog.highroad.ui.components.AppBar
import kotlinx.coroutines.launch

@Composable
fun BluetoothScreen(
    onNavigateUp: () -> Unit = {},
    bluetooth: BluetoothContainer,
    viewModel: BluetoothCaseViewModel = hiltViewModel()
) {
    viewModel.checkBluetoothState(bluetooth.service.getStatus())

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(id = BluetoothCase.titleRes),
                onBackPressed = onNavigateUp
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp)
            ) {
                if (viewModel.uiState.supported) {
                    if (viewModel.uiState.enabled) {
                        Text(text = "Bluetooth support!")

                        Spacer(modifier = Modifier.padding(top = 16.dp))
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    val list = bluetooth.service.getPairedDevices()
                                    viewModel.statePairList(list)
                                }
                            }
                        ) {
                            Text(text = "Show list paired bluetooth devices")
                        }

                        if (viewModel.uiState.pairDevices.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
                                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
                            ) {
                                items(viewModel.uiState.pairDevices) {
                                    Text(text = "Find device: ${it.name} ${it.adress}")
                                }
                            }
                        }

                        Spacer(modifier = Modifier.padding(top = 16.dp))
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    bluetooth.service.discoverDevices()
                                }
                            }
                        ) {
                            Text(text = "Find bluetooth devices")
                        }
                    } else {
                        Text(text = "You must Enable bluetooth")
                    }
                } else {
                    Text(text = "Bluetooth devices not support")
                }
                /*Spacer(modifier = Modifier.padding(top = 16.dp))
        Button(
            onClick = {
                viewModel.checkBluetoothState(bluetooth.service.getStatus())
            }
        ) {
            Text(text = "Check bluetooth")
        }*/
            }
        }
    }
}