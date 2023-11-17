package com.protsprog.highroad.bluetoothcase

import android.annotation.SuppressLint
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
    val address: String = "",
    val boundState: Int = 0
)

data class BTChatMessages(
    val author: String,
    val owner: Boolean,
    val message: String,
    val timestamp: Int,
    val date: String
)

@SuppressLint("MissingPermission")
fun BluetoothDevice.toState() = StatePairDevice(
    name = name ?: "",
    address = address ?: "",
    boundState = bondState
)

data class BluetoothUIState(
    var supported: Boolean = false,
    var enabled: Boolean = false,
    var finding: Boolean = false,
    var showToast: Boolean = false,
    var errorMessage: String = "",
    var showThrowing: Boolean = false,
    var deviceToThrow: StatePairDevice? = null,
    var pairDevices: List<StatePairDevice> = emptyList(),
    var foundDevices: List<StatePairDevice> = emptyList(),
    var chatMessages: List<BTChatMessages> = emptyList(),
    var messageToSend: String = ""
)

@HiltViewModel
class BluetoothCaseViewModel @Inject constructor() : ViewModel() {

    /*
        private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    to USE:
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    if (uiState.isUserLoggedIn) {
                        // Navigate to the Home screen.
     */
    var uiState by mutableStateOf(BluetoothUIState())

    fun setBluetoothState(status: BluetoothStatus) {
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
        uiState = uiState.copy(pairDevices = list, foundDevices = emptyList())
    }

    fun stateFoundList(list: List<StatePairDevice>) {
        uiState = uiState.copy(foundDevices = list, pairDevices = emptyList())
    }

    fun showDiscovering(start: Boolean) {
        uiState = uiState.copy(finding = start)
    }

    fun setError(error: String) {
        uiState = uiState.copy(
            showToast = true,
            errorMessage = error
        )
    }

    fun unsetShowToast() {
        uiState = uiState.copy(showToast = false)
    }

    fun setThrowingState(device: StatePairDevice) {
        uiState = uiState.copy(
            foundDevices = emptyList(),
            pairDevices = emptyList(),
            chatMessages = emptyList(),
            showThrowing = true,
            deviceToThrow = device
        )
    }

    fun unsetThrowingState() {
        uiState = uiState.copy(
            showThrowing = false,
            deviceToThrow = null
        )
    }

    fun addChatMessage(btChatMessages: BTChatMessages) {
        var list = uiState.chatMessages
        list += btChatMessages

        uiState = uiState.copy(chatMessages = list)
    }

    fun clearMessageToSend() {
        uiState = uiState.copy(messageToSend = "")
    }

    fun changeMessageToSend(s: String) {
        uiState = uiState.copy(messageToSend = s)
    }
}