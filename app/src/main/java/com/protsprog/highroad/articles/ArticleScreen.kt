package com.protsprog.highroad.articles

/*
READ
https://foso.github.io/Jetpack-Compose-Playground/foundation/shape/
https://developer.android.com/jetpack/compose/modifiers-list#Size
https://developer.android.com/jetpack/compose/animation/composables-modifiers#animatedvisibility
https://developer.android.com/reference/kotlin/androidx/compose/material/pullrefresh/package-summary
 */
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.protsprog.highroad.R
import com.protsprog.highroad.articles.ui.components.ArticleList
import com.protsprog.highroad.articles.ui.components.NetworkErrorIndicator
import com.protsprog.highroad.articles.ui.theme.ArticlesTheme
import com.protsprog.highroad.data.model.ArticleAnonce

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArticleScreen(
    windowWidthClass: WindowWidthSizeClass,
    viewModel: ArticleViewModel = hiltViewModel()
) {
    val uiState by viewModel.listUiState.collectAsStateWithLifecycle()
    val verticalView =
        remember { derivedStateOf { windowWidthClass == WindowWidthSizeClass.Compact } }
    val space = if (verticalView.value) 16.dp else 24.dp

    val state = rememberPullRefreshState(
        refreshing = uiState.isRefreshing,
        onRefresh = viewModel::refreshing
    )
    Box(
        modifier = Modifier
            .pullRefresh(state)
            .fillMaxSize()
    ) {
        Column {
            NetworkErrorIndicator(
                space = space,
                visibility = uiState.isServiceError,
                onClick = viewModel::refreshing
            )
            ArticleList(
                articles = uiState.articleList,
                space = space,
                verticalView = verticalView.value
            )
        }
        PullRefreshIndicator(
            refreshing = uiState.isRefreshing,
            state = state,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewArticleScreen() {
    ArticlesTheme {
//        ArticleScreen()
    }
}