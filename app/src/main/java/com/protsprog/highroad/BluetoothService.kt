package com.protsprog.highroad

/*
TO READ

https://developer.android.com/develop/connectivity/bluetooth/find-bluetooth-devices

 */
import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.STATE_ON
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.protsprog.highroad.bluetoothcase.StatePairDevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class BluetoothStatus { NOT_SUPPORT, SUPPORT_OFF, SUPPORT_ON }

const val TAG_BLUETOOTH_TEST = "TEST_BLUETOOTH"

interface BluetoothContainer {
    val service: BluetoothService
}

class BluetoothServiceImpl(private val context: Context) : BluetoothContainer {
    override val service: BluetoothService by lazy {
        BluetoothService(context)
    }
}

class BluetoothService(
    private val context: Context
) {
    private val bluetoothManager: BluetoothManager? =
        getSystemService(context, BluetoothManager::class.java)

    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.getAdapter()

    val listFoundDevice = mutableListOf<StatePairDevice>()
    fun getStatus(): BluetoothStatus = if (bluetoothAdapter != null) {
        if (bluetoothAdapter?.isEnabled == false) {
            BluetoothStatus.SUPPORT_OFF
        } else {
            BluetoothStatus.SUPPORT_ON
        }
    } else {
        BluetoothStatus.NOT_SUPPORT
    }

    fun getPairedDevices(): List<StatePairDevice> {
        val listDevice = mutableListOf<StatePairDevice>()

        if (bluetoothAdapter?.state == STATE_ON) {
            val pairedDevices: Set<BluetoothDevice>? = if (ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.BLUETOOTH_CONNECT
                ) == PackageManager.PERMISSION_GRANTED
            ) bluetoothAdapter?.bondedDevices else null

            pairedDevices?.forEach { device ->
                listDevice.add(
                    StatePairDevice(
                        name = device.name,
                        adress = device.address,
                        boundState = device.bondState
                    )
                )
//                Log.d(TAG_BLUETOOTH_TEST, "device name: ${device.name}")
//                Log.d(TAG_BLUETOOTH_TEST, "device type: ${device.type}")
//                Log.d(TAG_BLUETOOTH_TEST, "device bondState: ${device.bondState}")
            }
        }

        return listDevice.toList()
    }

    suspend fun discoverDevices() {
        withContext(Dispatchers.IO) {
            launch {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH_SCAN
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    listFoundDevice.clear()
                    bluetoothAdapter?.startDiscovery()
                    delay(15000L)
                    bluetoothAdapter?.cancelDiscovery()
                }
            }
        }
    }

    fun tests() {
//        ready to do
        if (bluetoothAdapter?.state == STATE_ON) {

        }
        Log.d(TAG_BLUETOOTH_TEST, "state: ${bluetoothAdapter?.state}")
        Log.d(TAG_BLUETOOTH_TEST, "isEnabled: ${bluetoothAdapter?.isEnabled}")
//        Log.d(TAG_BLUETOOTH_TEST, "result code: ${bluetoothManager?.}")
    }

}

