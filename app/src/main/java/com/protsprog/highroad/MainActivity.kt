package com.protsprog.highroad

/*
READ
https://developer.android.com/training/dependency-injection/hilt-android#define-bindings
https://developer.android.com/reference/kotlin/androidx/activity/ComponentActivity
https://developer.android.com/reference/android/bluetooth/BluetoothAdapter#ACTION_DISCOVERY_STARTED

https://m3.material.io/foundations/layout/applying-layout/window-size-classes

https://github.com/android/compose-samples/blob/main/JetNews

bluetooth
https://developer.android.com/develop/connectivity/bluetooth/setup

camera
https://developer.android.com/training/camerax
https://developer.android.com/codelabs/camerax-getting-started#1

permissions
https://developer.android.com/codelabs/android-privacy-codelab#3
https://developer.android.com/training/permissions/requesting#principles
 */
import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.app.AppOpsManager.OPSTR_CAMERA
import android.app.AppOpsManager.OPSTR_COARSE_LOCATION
import android.app.AsyncNotedAppOp
import android.app.SyncNotedAppOp
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_FINISHED
import android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_STARTED
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothDevice.ACTION_BOND_STATE_CHANGED
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import com.protsprog.highroad.authentication.BiometricCipher
import com.protsprog.highroad.bluetoothcase.toState
import com.protsprog.highroad.nav.HighroadNavigation
import com.protsprog.highroad.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

enum class IMAGE_SOURCES {
    CAMERA, MEDIA
}

@AndroidEntryPoint
//class MainActivity : ComponentActivity() {
class MainActivity : AppCompatActivity() {

    lateinit var bluetoothService: BluetoothContainer
    lateinit var cameraXService: CameraXContainer
    lateinit var photoPickerService: PhotoPickerContainer

//    Camera
    private val activityCameraXResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            cameraXService.service.checkPermissions(permissions)
        }

    //    access to MEDIA
    private val activityPhotoPickerRequestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            photoPickerService.service.checkPermissions(permissions)
        }

    private val activityPhotoPickerRequestPickMediaLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            photoPickerService.service.processMedia(uri)
        }


    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
//        (application as HighroadApplication).todoAppComponent.inject(this)
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val appOpsManager = getSystemService(AppOpsManager::class.java) as AppOpsManager
            appOpsManager.setOnOpNotedCallback(mainExecutor, DataAccessAuditListener)
        }

        bluetoothService = BluetoothServiceImpl(this)
        cameraXService = CameraXServiceImpl(this, activityCameraXResultLauncher)
        photoPickerService = PhotoPickerServiceImpl(
            this,
            activityPhotoPickerRequestPermissionLauncher,
            activityPhotoPickerRequestPickMediaLauncher
        )

// Register for broadcasts when a device is discovered.
        val filterBTFound = IntentFilter(BluetoothDevice.ACTION_FOUND)
        val filterBTDiscoveryStart = IntentFilter(ACTION_DISCOVERY_STARTED)
        val filterBTDiscoveryFinish = IntentFilter(ACTION_DISCOVERY_FINISHED)
        val filterBTBondState = IntentFilter(ACTION_BOND_STATE_CHANGED)

        registerReceiver(receiver, filterBTFound)
        registerReceiver(receiver, filterBTDiscoveryStart)
        registerReceiver(receiver, filterBTDiscoveryFinish)
        registerReceiver(receiver, filterBTBondState)

        cameraXService.service.initExecutor()

//        WindowCompat.setDecorFitsSystemWindows(window, false)
        val devicePostureFlow = getDevicePosture()

        setContent {
            HighroadNavigation(
                windowWidthClass = calculateWindowSizeClass(this).widthSizeClass,
                devicePosture = devicePostureFlow.collectAsState().value,
                biometricCipher = BiometricCipher(applicationContext, this),
                startRoute = intent.getStringExtra("insertDestination"),
                bluetooth = bluetoothService,
                checkPermissionBluetooth = {checkPermissionBluetooth()},
                cameraX = cameraXService,
                photoPicker = photoPickerService
            )
        }
    }

    /**
     * Flow of [DevicePosture] that emits every time there's a change in the windowLayoutInfo
     */
    private fun getDevicePosture() = WindowInfoTracker.getOrCreate(this).windowLayoutInfo(this)
        .flowWithLifecycle(this.lifecycle).map { layoutInfo ->
            val foldingFeature =
                layoutInfo.displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()
            when {
                isBookPosture(foldingFeature) -> DevicePosture.BookPosture(foldingFeature.bounds)

                isSeparating(foldingFeature) -> DevicePosture.Separating(
                    foldingFeature.bounds, foldingFeature.orientation
                )

                else -> DevicePosture.NormalPosture
            }
        }.stateIn(
            scope = lifecycleScope,
            started = SharingStarted.Eagerly,
            initialValue = DevicePosture.NormalPosture
        )

    private fun checkPermissionBluetooth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestMultiplePermissions.launch(
                arrayOf(
                    android.Manifest.permission.BLUETOOTH_SCAN,
                    android.Manifest.permission.BLUETOOTH_CONNECT
                )
            )
        } else {
            showEnableBluetoothDialog()
        }
    }

    private fun showEnableBluetoothDialog() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        requestBluetooth.launch(enableBtIntent)
    }

    private var requestBluetooth =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            Log.d(TAG_BLUETOOTH_TEST, "result code: ${result.resultCode}")
//            todo ???

            if (result.resultCode == RESULT_OK) {
                //granted
            } else {
                //deny
//                coroutineScope.launch {
//                    snackbarHostState.showSnackbar("Camera currently disabled due to denied permission.")
//                }
            }
        }

    //    Show permission dialog
    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var successPermisions = true
            permissions.entries.forEach {
                if (!it.value) {
                    successPermisions = false
                }
//                Log.d(TAG_BLUETOOTH_TEST, "permission: ${it.key} = ${it.value}")
            }
//            if permission OK, then show bluetooth ON dialog
            if (successPermisions && bluetoothService.service.getStatus() == BluetoothStatus.SUPPORT_OFF) {
                showEnableBluetoothDialog()
            }
        }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
//            Log.d(TAG_BLUETOOTH_TEST, "action: ${action}")
            when (action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

                    device?.let {
//                        Log.d(TAG_BLUETOOTH_TEST, "device: ${it.name} -> ${it.address}")

                        bluetoothService.service.showDiscoveredDevices(it.toState())
                    }
                }

                ACTION_DISCOVERY_STARTED -> {
                    bluetoothService.service.startDiscovering()
//                    Log.d(TAG_BLUETOOTH_TEST, "action A: ${action}")
                }

                ACTION_DISCOVERY_FINISHED -> {
                    bluetoothService.service.finishDiscovering()
//                    Log.d(TAG_BLUETOOTH_TEST, "action B: ${action}")
                }

                ACTION_BOND_STATE_CHANGED -> {
//                    Log.d(TAG_BLUETOOTH_TEST, "action B: ${action}")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

// Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver)

        cameraXService.service.destroyExecutor()
    }
}

@RequiresApi(Build.VERSION_CODES.R)
object DataAccessAuditListener : AppOpsManager.OnOpNotedCallback() {
    override fun onNoted(op: SyncNotedAppOp) {
        Log.d("DataAccessAuditListener","Sync Private Data Accessed: ${op.op}")
    }

    override fun onSelfNoted(op: SyncNotedAppOp) {
        Log.d("DataAccessAuditListener","Self Private Data accessed: ${op.op}")
    }

    override fun onAsyncNoted(asyncNotedAppOp: AsyncNotedAppOp) {
        var emoji = when (asyncNotedAppOp.op) {
            OPSTR_COARSE_LOCATION -> "\uD83D\uDDFA"
            OPSTR_CAMERA -> "\uD83D\uDCF8"
            else -> "?"
        }

        Log.d("DataAccessAuditListener", "Async Private Data ($emoji) Accessed: ${asyncNotedAppOp.op}")
    }
}