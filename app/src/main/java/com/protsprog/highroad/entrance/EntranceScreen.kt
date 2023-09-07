package com.protsprog.highroad.entrance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.AuthUIState
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
    navigations: Map<String, () -> Unit>,
    hasBack: Boolean = false,
    onBackPressed: () -> Unit,
    onClickLogin: () -> Unit,
    onClickProfile: () -> Unit,
    userUIState: AuthUIState,
) {
    val verticalView =
        remember { derivedStateOf { windowWidthClass == WindowWidthSizeClass.Compact } }
    val space = if (verticalView.value) 16.dp else 24.dp

    var showLandingScreen by remember { mutableStateOf(true) }
    if (showLandingScreen) {
        LandingEntranceScreen(onTimeout = { showLandingScreen = false })
    } else {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            scaffoldState = scaffoldState,
            topBar = {
                AuthAppBar(
                    title = Entrance.title,
                    scrollBehavior = scrollBehavior,
                    hasBack = hasBack,
                    hasAuth = userUIState.hasAuth,
                    userName = userUIState.user.name,
                    userEmail = userUIState.user.email,
                    onBackPressed = onBackPressed,
                    onClickLogin = onClickLogin,
                    onClickProfile = onClickProfile
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.padding(innerPadding),
                contentPadding = PaddingValues(
                    horizontal = space,
                    vertical = space
                ),
                verticalArrangement = Arrangement.spacedBy(space)
            ) {
                items(entranceItems) { item ->
                    navigations.get(item.destination)?.let {
                        if (verticalView.value) {
                            EntranceCardVertical(
                                item = item,
                                onNavigationToScreen = it
                            )
                        } else {
                            EntranceCardHorizontal(
                                item = item,
                                onNavigationToScreen = it
                            )
                        }
                    }
                }
            }
        }
    }
}


