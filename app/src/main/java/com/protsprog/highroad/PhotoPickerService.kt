package com.protsprog.highroad

/*
TO READ

https://developer.android.com/training/data-storage/shared/photopicker
 */

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.Companion.isPhotoPickerAvailable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat

interface PhotoPickerContainer {
    val service: PhotoPickerService
}

class PhotoPickerServiceImpl(
    private val activityContext: Context,
    private val activityPermissionLauncher: ActivityResultLauncher<Array<String>>,
    private val activityPickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>
) : PhotoPickerContainer {
    override val service: PhotoPickerService by lazy {
        PhotoPickerService(activityContext, activityPermissionLauncher, activityPickerLauncher)
    }
}

class PhotoPickerService(
    private val activityContext: Context,
    private val activityPermissionLauncher: ActivityResultLauncher<Array<String>>,
    private val activityPickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>
) {
    var permissions by mutableStateOf(false)
    var outputFile by mutableStateOf<Uri>(Uri.EMPTY)

    private val pickerAvailable = isPhotoPickerAvailable(activityContext)

    fun checkPermissions(
        checkPermissions: Map<String, @JvmSuppressWildcards Boolean>
    ) {
        // Handle Permission granted/rejected
        var permissionGranted = true
        checkPermissions.entries.forEach {
            if (it.key in REQUIRED_PERMISSIONS && it.value == false)
                permissionGranted = false
        }
        if (!permissionGranted) {
            permissions = false

            Toast.makeText(
                activityContext,
                "Permission photo picker request denied",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            permissions = true
        }
    }

    fun launchPickMedia() {
        activityPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    fun processMedia(uri: Uri?) {
        outputFile = uri ?: Uri.EMPTY
    }

    fun checkPermissionMedia() {
        if (pickerAvailable) {
            if (allPermissionsGranted()) {
                permissions = true
            } else {
                requestPermissions()
            }
        } else {
            permissions = false

            Toast.makeText(
                activityContext,
                "Photo picker not available",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun requestPermissions() {
        activityPermissionLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            activityContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val TAG = "PHOTO_PICKER"
        private val REQUIRED_PERMISSIONS =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(
                    android.Manifest.permission.READ_MEDIA_IMAGES

                )
            } else {
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
    }

    fun clearOutputFile() {
        outputFile = Uri.EMPTY
    }

}