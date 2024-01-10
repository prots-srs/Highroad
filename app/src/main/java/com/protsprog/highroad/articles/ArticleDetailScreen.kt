package com.protsprog.highroad.articles

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.protsprog.highroad.R
import com.protsprog.highroad.articles.ui.components.ArticleDetailItem
import com.protsprog.highroad.articles.ui.components.NetworkErrorIndicator
import com.protsprog.highroad.authentication.AuthServices
import com.protsprog.highroad.ui.components.AuthAppBar
import com.protsprog.highroad.ui.components.TOP_BAR_MENU_ACTIONS

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(
    modifier: Modifier = Modifier,
    windowWidthClass: WindowWidthSizeClass,
    viewModel: ArticleViewModel = hiltViewModel(),
    hasBack: Boolean = false,
    onBackPressed: () -> Unit,
    authService: AuthServices,
    itemId: Int = 0,
    navigateToEdit: (Int) -> Unit,
//    navigationToList: () -> Unit
) {
    var fetchList by rememberSaveable { mutableStateOf(false) }
    if (!fetchList) {
        fetchList = true
        viewModel.fetchItem(itemId)
        viewModel.fetchPermisions()

//        Log.d("TEST_FLOW", "comp: fetching")
    }

//    Log.d("TEST_FLOW", "composable item detail: ${viewModel.articleItem}")

    LaunchedEffect(key1 = authService.hasAuthorization) {
//        Log.d("TEST_FLOW", "comp: LaunchedEffect ${authService.hasAuthorization}")
        if (authService.hasAuthorization) {
            viewModel.fetchPermisions()
        } else {
            viewModel.breakPermissions()
        }
    }

//    Log.d(
//        "TEST_FLOW",
//        "composable needGoToListScreen: ${viewModel.serviceUiState.needGoToListScreen}"
//    )

    LaunchedEffect(key1 = viewModel.serviceUiState.needGoToListScreen) {
        if (viewModel.serviceUiState.needGoToListScreen) {
            onBackPressed()
        }
    }

//    Log.d("TEST_FLOW", "comp: permission edit ${viewModel.permissionUiState.update}")
//    Log.d("TEST_FLOW", "comp: permission delete ${viewModel.permissionUiState.delete}")
//    Log.d("TEST_FLOW", "comp: edit mode ${viewModel.uiState.editMode}")

    val verticalView =
        remember { derivedStateOf { windowWidthClass == WindowWidthSizeClass.Compact } }
    val step = if (verticalView.value) 2 else 3

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

//    val state = rememberPullRefreshState(
//        refreshing = viewModel.commonUiState.isRefreshing,
//        onRefresh = { viewModel.refreshItem(itemId) }
//    )

    Scaffold(modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
        AuthAppBar(
            title = viewModel.articleItem.title,
            scrollBehavior = scrollBehavior,
            hasBack = hasBack,
            authService = authService,
            onBackPressed = onBackPressed,
            actions = mapOf(TOP_BAR_MENU_ACTIONS.RELOAD to { viewModel.refreshItem(itemId) },
                TOP_BAR_MENU_ACTIONS.DELETE to { viewModel.deleteItem(itemId) })
        )
    }, floatingActionButton = {
        if (viewModel.permissionUiState.update) {
            FloatingActionButton(
                onClick = { navigateToEdit(itemId) },
            ) {
                Icon(Icons.Outlined.Edit, "Edit")
            }
        }
    }) { innerPadding ->

        Box(
            modifier = Modifier
//                .pullRefresh(state)
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_medium))
            ) {
                item {
                    ArticleDetailItem(
                        publish = viewModel.articleItem.publish,
                        title = viewModel.articleItem.title,
                        picture = viewModel.articleItem.picture,
                        description = viewModel.articleItem.description
                    )
                }
            }

//            AnimatedVisibility(visible = viewModel.commonUiState.hasServiceError) {
//                NetworkErrorIndicator(step = step)
//            }

//            PullRefreshIndicator(
//                refreshing = viewModel.commonUiState.isRefreshing,
//                state = state,
//                modifier = Modifier.align(Alignment.TopCenter)
//            )
        }
    }
}
