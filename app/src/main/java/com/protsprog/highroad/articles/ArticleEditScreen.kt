package com.protsprog.highroad.articles

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.protsprog.highroad.CameraXContainer
import com.protsprog.highroad.IMAGE_SOURCES
import com.protsprog.highroad.PhotoPickerContainer
import com.protsprog.highroad.R
import com.protsprog.highroad.articles.ui.components.ArticleDetailItemEdit
import com.protsprog.highroad.articles.ui.components.NetworkErrorIndicator
import com.protsprog.highroad.authentication.AuthServices
import com.protsprog.highroad.ui.components.AuthAppBar

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ArticleEditScreen(
    modifier: Modifier = Modifier,
    windowWidthClass: WindowWidthSizeClass,
    viewModel: ArticleViewModel = hiltViewModel(),
//    hasBack: Boolean = false,
//    onBackPressed: () -> Unit,
    authService: AuthServices,
    itemId: Int = 0,
    navigateToArticle: (Int) -> Unit,
    cameraX: CameraXContainer,
    photoPicker: PhotoPickerContainer
) {
    var fetchItem by remember { mutableStateOf(false) }
    if (!fetchItem) {
        fetchItem = true

//        Log.d("TEST_FLOW", "composable: itemId ${itemId}")
        viewModel.fetchItem(itemId, fillPutModel = true)
        viewModel.fetchPermisions()
    }

    LaunchedEffect(key1 = authService.hasAuthorization) {
//        Log.d("TEST_FLOW", "comp: LaunchedEffect ${authService.hasAuthorization}")
        if (!authService.hasAuthorization) {
//            onBackPressed()
            navigateToArticle(viewModel.articlePutItem.id)
        }
    }

    LaunchedEffect(key1 = viewModel.serviceUiState.needGoToDetailScreen) {
        if (viewModel.serviceUiState.needGoToDetailScreen) {
            navigateToArticle(viewModel.articlePutItem.id)
        }
    }

    val verticalView =
        remember { derivedStateOf { windowWidthClass == WindowWidthSizeClass.Compact } }
    val step = if (verticalView.value) 2 else 3

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val state = rememberPullRefreshState(
        refreshing = viewModel.serviceUiState.hasRefreshing,
        onRefresh = { }
    )

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AuthAppBar(
                title = if (viewModel.articleItem.title.isNotEmpty()) viewModel.articleItem.title else "Compose article",
                scrollBehavior = scrollBehavior,
                hasBack = true,
                authService = authService,
                onBackPressed = { navigateToArticle(viewModel.articlePutItem.id) },
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .pullRefresh(state)
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.padding_medium)),
//            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
            ) {
                item {
                    ArticleDetailItemEdit(
                        item = viewModel.articlePutItem,
                        changeItemPublish = viewModel::changeInPutItemPublish,
                        changeItemSort = viewModel::changeInPutItemSort,
                        changeItemTitle = viewModel::changeInPutItemTitle,
                        changeItemDescription = viewModel::changeInPutItemDescription,
                        changeItemPicture = viewModel::changeInPutItemPicture,
                        changeUseMedia = viewModel::changeUseMedia,
                        changeUseCamera = viewModel::changeUseCamera,
                        errors = viewModel.putErrors,
                        services = viewModel.serviceUiState,
                        onClickSubmit = viewModel::onClickSubmit,
                        checkPermissionCameraX = { cameraX.service.checkPermissionCameraX() },
                        permissionCamera = cameraX.service.permissions,
                        startCamera = { ctx, lc -> cameraX.service.startCamera(ctx, lc) },
                        takePhoto = { cameraX.service.takePhoto() },
                        checkPermissionPhotoPicker = { photoPicker.service.checkPermissionMedia() },
                        permissionMedia = photoPicker.service.permissions,
                        startPickPhoto = { photoPicker.service.launchPickMedia() },
                        outputFiles = mapOf(
                            IMAGE_SOURCES.CAMERA to cameraX.service.outputFile,
                            IMAGE_SOURCES.MEDIA to photoPicker.service.outputFile
                        ),
                        clearOutput = { source: IMAGE_SOURCES ->
                            when (source) {
                                IMAGE_SOURCES.CAMERA -> cameraX.service.clearOutputFile()
                                IMAGE_SOURCES.MEDIA -> photoPicker.service.clearOutputFile()
                            }
                        }
                    )
                }
            }

            AnimatedVisibility(visible = viewModel.serviceUiState.hasServiceError) {
                NetworkErrorIndicator(step = step)
            }

            PullRefreshIndicator(
                refreshing = viewModel.serviceUiState.hasRefreshing,
                state = state,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}
