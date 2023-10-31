package com.protsprog.highroad.bluetoothcase

import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.protsprog.highroad.BluetoothStatus
import com.protsprog.highroad.TAG_BLUETOOTH_TEST
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class StatePairDevice(
    val name: String = "",
    val adress: String = "",
    val boundState: Int = 0
)

data class BluetoothUIState(
    var supported: Boolean = false,
    var enabled: Boolean = false,
    var pairDevices: List<StatePairDevice> = emptyList()
)

@HiltViewModel
class BluetoothCaseViewModel @Inject constructor() : ViewModel() {

    var uiState by mutableStateOf(BluetoothUIState())

    fun checkBluetoothState(status: BluetoothStatus) {
        uiState = when (status) {
            BluetoothStatus.NOT_SUPPORT -> uiState.copy(
                supported = false,
                enabled = false
            )

            BluetoothStatus.SUPPORT_OFF -> uiState.copy(
                supported = true,
                enabled = false
            )

            BluetoothStatus.SUPPORT_ON -> uiState.copy(
                supported = true,
                enabled = true
            )
        }
    }

    fun statePairList(list: List<StatePairDevice>) {
        uiState = uiState.copy(pairDevices = list)
//        list?.forEach { device ->
//            Log.d(TAG_BLUETOOTH_TEST, "device name: ${device.name}")
//            Log.d(TAG_BLUETOOTH_TEST, "device bondState: ${device.boundState}")
//            Log.d(TAG_BLUETOOTH_TEST, "device address: ${device.adress}")
//        }
    }

}