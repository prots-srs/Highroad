package com.protsprog.highroad.articles

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import com.protsprog.highroad.authentication.ui.AuthServices
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
    itemId: Int = 0
) {
    var fetchList by rememberSaveable { mutableStateOf(false) }
    if (!fetchList) {
        fetchList = true
        viewModel.fetchItem(itemId)
    }

    val verticalView =
        remember { derivedStateOf { windowWidthClass == WindowWidthSizeClass.Compact } }
    val step = if (verticalView.value) 2 else 3

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AuthAppBar(
                title = viewModel.articleItem.title,
                scrollBehavior = scrollBehavior,
                hasBack = hasBack,
                authService = authService,
                onBackPressed = onBackPressed,
//                actions = mapOf(TOP_BAR_MENU_ACTIONS.RELOAD to { viewModel.fetchItem(itemId) })
            )
        }
    ) { innerPadding ->
        Text(viewModel.articleItem.description ?: "abc", modifier = modifier.padding(innerPadding))
    }
}
