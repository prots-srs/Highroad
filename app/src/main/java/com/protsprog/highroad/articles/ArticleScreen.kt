package com.protsprog.highroad.articles

/*
TO READ
https://foso.github.io/Jetpack-Compose-Playground/foundation/shape/

https://developer.android.com/jetpack/compose/modifiers-list#Size
https://developer.android.com/jetpack/compose/animation/composables-modifiers#animatedvisibility

https://developer.android.com/reference/kotlin/androidx/compose/material/pullrefresh/package-summary

to flow fron network and db
https://developer.android.com/codelabs/advanced-kotlin-coroutines?hl=en#0
 */
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.protsprog.highroad.R
import com.protsprog.highroad.articles.ui.components.ArticleList
import com.protsprog.highroad.articles.ui.components.FilterRow
import com.protsprog.highroad.articles.ui.components.NetworkErrorIndicator
import com.protsprog.highroad.authentication.AuthServices
import com.protsprog.highroad.nav.Articles
import com.protsprog.highroad.ui.components.AuthAppBar
import com.protsprog.highroad.ui.components.TOP_BAR_MENU_ACTIONS

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ArticleScreen(
    modifier: Modifier = Modifier,
    windowWidthClass: WindowWidthSizeClass,
    viewModel: ArticleViewModel = hiltViewModel(),
    hasBack: Boolean = false,
    onBackPressed: () -> Unit,
    authService: AuthServices,
    navigateToArticle: (Int) -> Unit,
    navigateToEdit: (Int) -> Unit
) {
    var fetchList by rememberSaveable { mutableStateOf(false) }
    if (!fetchList) {
        fetchList = true
        viewModel.fetchPermisions()
        viewModel.fetchList()
    }

    LaunchedEffect(key1 = authService.hasAuthorization) {
//        Log.d("TEST_FLOW", "comp: LaunchedEffect ${authService.hasAuthorization}")
        if (authService.hasAuthorization) {
            viewModel.fetchPermisions()
        } else {
            viewModel.breakPermissions()
        }
    }

    val verticalView =
        remember { derivedStateOf { windowWidthClass == WindowWidthSizeClass.Compact } }
    val step = if (verticalView.value) 2 else 3

    val state = rememberPullRefreshState(
        refreshing = viewModel.serviceUiState.hasRefreshing,
        onRefresh = viewModel::refreshList
    )

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AuthAppBar(
                title = stringResource(id = Articles.titleRes),
                scrollBehavior = scrollBehavior,
                hasBack = hasBack,
                authService = authService,
                onBackPressed = onBackPressed,
                actions = mapOf(TOP_BAR_MENU_ACTIONS.RELOAD to { viewModel.refreshList() })
            )
        },
        floatingActionButton = {
            if (viewModel.permissionUiState.update) {
                FloatingActionButton(
                    onClick = { navigateToEdit(0) },
                ) {
                    Icon(Icons.Outlined.Edit, "Compose")
                }
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .pullRefresh(state)
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(all = dimensionResource(id = R.dimen.padding_small) * step)
            ) {
                if (viewModel.permissionUiState.update) {
                    FilterRow(
                        label = "Sorting by sort",
                        checked = viewModel.commonUiState.sortBySortAsc,
                        onCheckedChange = viewModel::sortBySort
                    )
                    FilterRow(
                        label = "Show only published",
                        checked = viewModel.commonUiState.showOnlyPublished,
                        onCheckedChange = viewModel::showOnlyPublished
                    )
                    Spacer(modifier.height(dimensionResource(id = R.dimen.padding_small)))
                }
                ArticleList(
                    articles = viewModel.articleList,
                    navigateToArticle = navigateToArticle,
                    step = step,
                    verticalView = verticalView.value
                )
            }
//            Log.d("TEST_FLOW", "comp: service error ${viewModel.uiState.hasServiceError}")
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