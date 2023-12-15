package com.protsprog.highroad

/*
TO READ

https://proandroiddev.com/compose-camerax-on-android-58578f37e6df

https://developer.android.com/codelabs/camerax-getting-started4
 */
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.LifecycleOwner
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

typealias LumaListener = (luma: Double) -> Unit

interface CameraXContainer {
    val service: CameraXService
}

class CameraXServiceImpl(
    private val context: Context,
    private val activityCameraXResultLauncher: ActivityResultLauncher<Array<String>>
) : CameraXContainer {
    override val service: CameraXService by lazy {
        CameraXService(context, activityCameraXResultLauncher)
    }
}

class CameraXService(
    private val activityContext: Context,
    private val activityCameraXResultLauncher: ActivityResultLauncher<Array<String>>
) {
    var permissions by mutableStateOf(false)
    var capturingVideo by mutableStateOf(false)
    var outputFile by mutableStateOf<Uri>(Uri.EMPTY)

    private var imageCapture: ImageCapture? = null

    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null

    private lateinit var cameraExecutor: ExecutorService

    fun initExecutor() {
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    fun destroyExecutor() {
        cameraExecutor.shutdown()
    }

    fun checkPermissions(
        permissions: Map<String, @JvmSuppressWildcards Boolean>
    ) {
        // Handle Permission granted/rejected
        var permissionGranted = true
        permissions.entries.forEach {
            if (it.key in REQUIRED_PERMISSIONS_CAMERA && it.value == false) permissionGranted =
                false
        }
        if (!permissionGranted) {
            this.permissions = false

            Toast.makeText(
                activityContext, "Permission camera request denied", Toast.LENGTH_SHORT
            ).show()
        } else {
            this.permissions = true
//            startCamera()
        }

    }

    private fun requestCameraPermissions() {
        activityCameraXResultLauncher.launch(REQUIRED_PERMISSIONS_CAMERA)
    }

    private fun allCameraPermissionsGranted() = REQUIRED_PERMISSIONS_CAMERA.all {
        ContextCompat.checkSelfPermission(
            activityContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS_CAMERA = mutableListOf(
            android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }

    fun checkPermissionCameraX() {
        if (allCameraPermissionsGranted()) {
            permissions = true

//            Log.d("CameraXApp", "service permissions: ${permissionCamera}")
//            startCamera()
        } else {
            requestCameraPermissions()
        }
    }

    fun startCamera(
        factoryContext: Context,
//        localComposeContext: Context,
        lifecycleOwner: LifecycleOwner
    ): PreviewView {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(activityContext)

        val previewView = PreviewView(factoryContext)
        val executor = ContextCompat.getMainExecutor(factoryContext)

        cameraProviderFuture.addListener({

            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build().also {
                    it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { luma ->
//                        Log.d(TAG, "Average luminosity: $luma")
                    })
                }

            val recorder =
                Recorder.Builder().setQualitySelector(QualitySelector.from(Quality.HIGHEST)).build()
            videoCapture = VideoCapture.withOutput(recorder)

            val cameraSelector =
                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    videoCapture,
                    imageCapture,
//                    imageAnalyzer
                )
            } catch (exc: Exception) {
//                Log.d(TAG, "Use case binding failed", exc)
            }
        }, executor)

        return previewView
    }


    fun takePhoto() {

//        Log.d(TAG, "take photo ${imageCapture}")

// Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            activityContext.contentResolver,
//                contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
        ).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(outputOptions,
            ContextCompat.getMainExecutor(activityContext),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
//                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    outputFile = output.savedUri ?: Uri.EMPTY
//                    val msg = "Photo capture succeeded: ${output.savedUri}"
//                    Toast.makeText(activityContext, msg, Toast.LENGTH_SHORT).show()
//                    Log.d(TAG, msg)
                }
            })
    }

    fun captureVideo() {

//        Log.d(TAG, "captureVideo ${videoCapture}")

        val videoCapture = videoCapture ?: return

        capturingVideo = true

        val curRecording = recording
        if (curRecording != null) {
            // Stop the current recording session.
            curRecording.stop()
            recording = null
            return
        }

        // create and start a new recording session
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video")
            }
        }

        val mediaStoreOutputOptions = MediaStoreOutputOptions.Builder(
            activityContext.contentResolver,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        ).setContentValues(contentValues).build()

        recording =
            videoCapture.output.prepareRecording(activityContext, mediaStoreOutputOptions).apply {
                if (PermissionChecker.checkSelfPermission(
                        activityContext, android.Manifest.permission.RECORD_AUDIO
                    ) == PermissionChecker.PERMISSION_GRANTED
                ) {
                    withAudioEnabled()
                }
            }.start(ContextCompat.getMainExecutor(activityContext)) { recordEvent ->
                when (recordEvent) {
                    is VideoRecordEvent.Start -> {
//                        viewBinding.videoCaptureButton.apply {
//                            text = getString(R.string.stop_capture)
//                            isEnabled = true
//                        }

                        capturingVideo = false
                    }

                    is VideoRecordEvent.Finalize -> {
                        if (!recordEvent.hasError()) {
                            val msg =
                                "Video capture succeeded: " + "${recordEvent.outputResults.outputUri}"
                            Toast.makeText(activityContext, msg, Toast.LENGTH_SHORT).show()
//                            Log.d(TAG, msg)
                        } else {
                            recording?.close()
                            recording = null
//                            Log.e(TAG, "Video capture ends with error: " + "${recordEvent.error}")
                        }
                        capturingVideo = false

//                        viewBinding.videoCaptureButton.apply {
//                            text = getString(R.string.start_capture)
//                            isEnabled = true
//                        }
                    }
                }
            }
    }

    fun clearOutputFile() {
        outputFile = Uri.EMPTY
    }
}

private class LuminosityAnalyzer(private val listener: LumaListener) : ImageAnalysis.Analyzer {

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()    // Rewind the buffer to zero
        val data = ByteArray(remaining())
        get(data)   // Copy the buffer into a byte array
        return data // Return the byte array
    }

    override fun analyze(image: ImageProxy) {

        val buffer = image.planes[0].buffer
        val data = buffer.toByteArray()
        val pixels = data.map { it.toInt() and 0xFF }
        val luma = pixels.average()

        listener(luma)

        image.close()
    }
}