package com.protsprog.highroad

/*
TO READ

https://developer.android.com/develop/connectivity/bluetooth/find-bluetooth-devices

https://www.uuidgenerator.net/

https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/

 */
import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.STATE_ON
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.protsprog.highroad.bluetoothcase.BTChatMessages
import com.protsprog.highroad.bluetoothcase.BluetoothCaseViewModel
import com.protsprog.highroad.bluetoothcase.StatePairDevice
import com.protsprog.highroad.bluetoothcase.toState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.UUID

enum class BluetoothStatus { NOT_SUPPORT, SUPPORT_OFF, SUPPORT_ON }

const val TAG_BLUETOOTH_TEST = "TEST_BLUETOOTH"

interface BluetoothContainer {
    val service: BluetoothService
}

private val UUID_DEVICE = "f03ffc86-9b38-412c-8104-b3aa64f83d0d"

class BluetoothServiceImpl(private val context: Context) : BluetoothContainer {
    override val service: BluetoothService by lazy {
        BluetoothService(context)
    }
}

class BluetoothService(
    private val context: Context
) {
    private val scope = CoroutineScope(Job() + Dispatchers.IO)
    private var catchJob: Job? = null

    private val bluetoothManager: BluetoothManager? =
        getSystemService(context, BluetoothManager::class.java)

    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.getAdapter()

    private lateinit var viewModel: BluetoothCaseViewModel

    private val listFoundDevice = mutableListOf<StatePairDevice>()
    private var connectedDevice: BluetoothDevice? = null

    private var serverThread: AcceptThread? = null
    private var messageToSend: String = ""

    private val datetimeFormat = DateTimeFormatter.ofLocalizedDateTime(
        FormatStyle.SHORT,//LONG
        FormatStyle.MEDIUM
    )

    fun injectViewModule(vm: BluetoothCaseViewModel) {
        viewModel = vm
    }

    fun checkStatus() {
        viewModel.setBluetoothState(getStatus())
    }

    fun getStatus(): BluetoothStatus = if (bluetoothAdapter != null) {
        if (bluetoothAdapter?.isEnabled == false) {
            BluetoothStatus.SUPPORT_OFF
        } else {
            BluetoothStatus.SUPPORT_ON
        }
    } else {
        BluetoothStatus.NOT_SUPPORT
    }

    fun actionShowPairedDevices() {
        val listDevice = mutableListOf<StatePairDevice>()

        if (bluetoothAdapter?.state == STATE_ON) {
            val pairedDevices: Set<BluetoothDevice>? = if (ActivityCompat.checkSelfPermission(
                    context, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        Manifest.permission.BLUETOOTH_CONNECT
                    } else {
                        Manifest.permission.BLUETOOTH
                    }
                ) == PackageManager.PERMISSION_GRANTED
            ) bluetoothAdapter?.bondedDevices else null

            pairedDevices?.forEach { device ->
                listDevice.add(device.toState())
            }

            closeThrowing()
        }

        viewModel.statePairList(listDevice.toList())
    }

    fun actionDiscoverDevices() {
        if (ActivityCompat.checkSelfPermission(
                context,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    Manifest.permission.BLUETOOTH_SCAN
                } else {
                    Manifest.permission.BLUETOOTH_ADMIN
                }
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            closeThrowing()
            listFoundDevice.clear()
            bluetoothAdapter?.startDiscovery()
        }
    }

    fun showDiscoveredDevices(device: StatePairDevice) {
        listFoundDevice.add(device)
        viewModel.stateFoundList(listFoundDevice)
    }

    fun startDiscovering() {
        viewModel.showDiscovering(true)
    }

    fun finishDiscovering() {
        viewModel.showDiscovering(false)
        adapterCancelDiscovery()
    }

    private fun adapterCancelDiscovery() {
        if (ActivityCompat.checkSelfPermission(
                context,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    Manifest.permission.BLUETOOTH_SCAN
                } else {
                    Manifest.permission.BLUETOOTH_ADMIN
                }
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            bluetoothAdapter?.cancelDiscovery()
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

    fun createBond(address: String) {
        try {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        Manifest.permission.BLUETOOTH_CONNECT
                    } else {
                        Manifest.permission.BLUETOOTH_ADMIN
                    }
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val device = bluetoothAdapter?.getRemoteDevice(address)
                if (!(device?.createBond() ?: false)) {
                    viewModel.setError("Wrong bond device")
                }
            }
        } catch (e: IOException) {
            viewModel.setError(e.message.toString())
        }
    }

    fun selectDevice(address: String) {
        try {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        Manifest.permission.BLUETOOTH_CONNECT
                    } else {
                        Manifest.permission.BLUETOOTH
                    }
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val device = bluetoothAdapter?.getRemoteDevice(address)
                connectedDevice = device
                device?.let {
                    viewModel.setThrowingState(it.toState())

                    catchJob = scope.launch {
                        serverThread = AcceptThread()
                        serverThread?.start()
                    }
                }

                if (device == null) {
                    viewModel.setError("Device ${address} not found")
                }
            }
        } catch (e: IOException) {
            viewModel.setError(e.message.toString())
        }
    }

    fun closeThrowing() {
        connectedDevice = null

        serverThread?.let { it.cancel() }
        catchJob?.let { it.cancel() }

        viewModel.unsetThrowingState()
    }

    fun onClickThrowMessage(message: String) {
        connectedDevice?.let { device ->
            messageToSend = message
            scope.launch {
                val client = ConnectThread(device)
                client.start()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private inner class AcceptThread : Thread() {

        private val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
            bluetoothAdapter?.listenUsingInsecureRfcommWithServiceRecord(
                "Highroad chat",
                UUID.fromString(UUID_DEVICE)
            )
        }

        override fun run() {
            super.run()

            var shouldLoop = true
            while (shouldLoop) {
                val socket: BluetoothSocket? = try {
                    mmServerSocket?.accept()
                } catch (eAccept: IOException) {
//                    Log.d(TAG_BLUETOOTH_TEST, "Socket's accept() method failed: ${eAccept.message}")
                    viewModel.setError("Server socket failed accept")
                    shouldLoop = false
                    null
                }

                socket?.also { btSocket ->

                    val mmInStream: InputStream? = btSocket.inputStream
                    val mmBuffer = ByteArray(102400)
                    var numBytes: Int = 0// bytes returned from read()
                    mmInStream?.let { iStream ->
                        while (true) {
                            numBytes = try {
//                                Log.d(TAG_BLUETOOTH_TEST, "iStream: ${iStream}")
                                iStream.read(mmBuffer)
                            } catch (eRead: IOException) {
//                                Log.d(TAG_BLUETOOTH_TEST, "read error: ${eRead.message}")
                                break
                            }
                        }
                    }

                    /*mmInStream?.let {
                        val reader = BufferedReader(it.reader())
                        var content: String = try {
                            reader.readText()
//                        } catch (eRead: IOException) {
//                            Log.d(TAG_BLUETOOTH_TEST, "error content: ${eRead.message}")
                        } finally {
                            reader.close()
                        }
                        Log.d(TAG_BLUETOOTH_TEST, "read content: ${content}")

                        /*try {
                            val content = it.bufferedReader().use(BufferedReader::readText)
                            Log.d(TAG_BLUETOOTH_TEST, "read content: ${content}")
                        } catch (eRead: IOException) {
                            Log.d(TAG_BLUETOOTH_TEST, "error content: ${eRead.message}")
                        }*/
                    }*/


//                    Log.d(TAG_BLUETOOTH_TEST, "read numBytes: ${numBytes}")
//                    Log.d(TAG_BLUETOOTH_TEST, "read: ${mmBuffer.decodeToString(0, numBytes)}")

                    val remoteDevice = btSocket.remoteDevice
                    var remoteDeviceName = ""
                    remoteDevice?.let {
                        remoteDeviceName = it.name ?: it.address
                    }
                    val dateTime = LocalDateTime.now()
                    viewModel.addChatMessage(
                        BTChatMessages(
                            author = remoteDeviceName,
                            owner = false,
                            message = mmBuffer.decodeToString(0, numBytes),
                            timestamp = dateTime.toEpochSecond(ZoneOffset.UTC).toInt(),
                            date = dateTime.format(datetimeFormat)
                        )
                    )

                    btSocket.close()
                }
            }
        }

        fun cancel() {
            try {
                mmServerSocket?.close()
            } catch (e: IOException) {
//                Log.d(TAG_BLUETOOTH_TEST, "Error close socket B: ${e.message}")
//                viewModel.setError("Could not close the client socket ${e.message}")
            }
        }
    }

    @SuppressLint("MissingPermission")
    private inner class ConnectThread(device: BluetoothDevice) : Thread() {

        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createRfcommSocketToServiceRecord(UUID.fromString(UUID_DEVICE))
        }

        override fun run() {
            super.run()
            adapterCancelDiscovery()

            mmSocket?.let { socket ->
                try {
                    socket.connect()
                    try {
//                        val theString = "Kotlin is awesome!"
//                        val inputStream = theString.byteInputStream()

                        val mmOutStream: OutputStream = socket.outputStream
                        mmOutStream.write(messageToSend.toByteArray())

                        Log.d(TAG_BLUETOOTH_TEST, "write: ${messageToSend}")

                        val dateTime = LocalDateTime.now()
                        viewModel.addChatMessage(
                            BTChatMessages(
                                author = "",
                                owner = true,
                                message = messageToSend,
                                timestamp = dateTime.toEpochSecond(ZoneOffset.UTC).toInt(),
                                date = dateTime.format(datetimeFormat)
                            )
                        )
                        viewModel.clearMessageToSend()
//                        messageToSend = ""
                    } catch (eWrite: IOException) {
                        viewModel.setError("Send error")
//                        Log.d(TAG_BLUETOOTH_TEST, "write error: ${eWrite.message}")
                    }

                } catch (eConnect: IOException) {
                    viewModel.setError("Error connect to socket")
//                    viewModel.setError("Error connect socket: ${eConnect.message}")
//                    Log.d(TAG_BLUETOOTH_TEST, "Error connect socket: ${eConnect.message}")
                }
            }

            cancel()
        }

        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
//                Log.d(TAG_BLUETOOTH_TEST, "Error close socket A: ${e.message}")
//                viewModel.setError("Error close socket: ${e.message}")
            }
        }
    }
}

