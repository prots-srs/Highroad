package com.protsprog.highroad.entrance

/*
TO READ

 */
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ButtonColors
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.protsprog.highroad.CameraXContainer
import com.protsprog.highroad.MainActivity
import com.protsprog.highroad.R
import com.protsprog.highroad.authentication.AuthServices
import com.protsprog.highroad.entrance.data.EntranceItem
import com.protsprog.highroad.entrance.ui.components.EntranceCardHorizontal
import com.protsprog.highroad.entrance.ui.components.EntranceCardVertical
import com.protsprog.highroad.nav.Entrance
import com.protsprog.highroad.ui.components.AuthAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntranceScreen(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    windowWidthClass: WindowWidthSizeClass,
    list: List<EntranceItem>,
    navigations: Map<String, () -> Unit>,
    hasBack: Boolean = false,
    onBackPressed: () -> Unit,
    authService: AuthServices,
) {

    val verticalView =
        remember { derivedStateOf { windowWidthClass == WindowWidthSizeClass.Compact } }
    val space = if (verticalView.value) 16.dp else 24.dp

    var showLandingScreen by remember { mutableStateOf(true) }
    if (showLandingScreen) {
        LandingEntranceScreen(onTimeout = { showLandingScreen = false })
    } else {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

        Scaffold(modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            scaffoldState = scaffoldState,
            topBar = {
                AuthAppBar(
                    title = Entrance.title,
                    scrollBehavior = scrollBehavior,
                    hasBack = hasBack,
                    authService = authService,
                    onBackPressed = onBackPressed,
                )
            }) { innerPadding ->

            LazyColumn(
                modifier = modifier.padding(innerPadding),
                contentPadding = PaddingValues(
                    horizontal = space,
                    vertical = space
                ),
                verticalArrangement = Arrangement.spacedBy(space)
            ) {
                items(list, key = { it.id }) { item ->
                    if (verticalView.value) {
                        EntranceCardVertical(
                            item = item,
                            onNavigationToScreen = navigations.get(item.destination) ?: {}
                        )
                    } else {
                        EntranceCardHorizontal(
                            item = item,
                            onNavigationToScreen = navigations.get(item.destination) ?: {}
                        )
                    }
                }
            }
        }
    }
}


