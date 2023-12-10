package com.protsprog.highroad.camerax

import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.protsprog.highroad.CameraXContainer
import com.protsprog.highroad.R
import com.protsprog.highroad.nav.CameraXCase
import com.protsprog.highroad.ui.components.AppBar

@Composable
fun CameraXScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    cameraX: CameraXContainer,
) {
    LaunchedEffect(Unit) {
        cameraX.service.checkPermissionCameraX()
    }

    val lifecycleOwner = LocalLifecycleOwner.current
//    val context = LocalContext.current

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(id = CameraXCase.titleRes),
                onBackPressed = onBackPressed,
            )
        }) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            AnimatedVisibility(visible = cameraX.service.permissionCamera) {
                Row(modifier = modifier.fillMaxWidth()) {
                    Column(modifier = modifier.fillMaxWidth()) {
//                        CameraXPreviewScreen(cameraX = cameraX)

                        AndroidView(
                            modifier = modifier
                                .fillMaxWidth()
                                .aspectRatio(1f, false)
//            .requiredHeight(360.dp)
                                .padding(all = dimensionResource(id = R.dimen.padding_large))
                                .clip(MaterialTheme.shapes.medium),
                            factory = { ctx ->
                                cameraX.service.startCamera(
                                    factoryContext = ctx,
//                                    localComposeContext = context,
                                    lifecycleOwner = lifecycleOwner
                                )
                            },
                        )

                        Button(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = dimensionResource(id = R.dimen.padding_large)),
                            onClick = {cameraX.service.takePhoto()},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary
                            )
                        ) {
                            Text("Take photo")
                        }

                        Button(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = dimensionResource(id = R.dimen.padding_large)),
                            onClick = {cameraX.service.captureVideo()},
                            enabled = !cameraX.service.capturingVideo,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary
                            )
                        ) {
                            Text("Start capture")
                        }

                    }
                }
            }
        }
    }
}