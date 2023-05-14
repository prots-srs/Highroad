package com.protsprog.highroad.entrance.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.protsprog.highroad.entrance.entranceItems
import com.protsprog.highroad.entrance.ui.components.EntranceCard
import com.protsprog.highroad.entrance.ui.theme.EntranceTheme
import com.protsprog.highroad.nav.navigateSingleTopTo

@Composable
fun EntranceScreen(nav:NavHostController) {
    EntranceTheme {
        LazyColumn {
            items(entranceItems) { item ->
                EntranceCard(
                    item = item,
//                    onNavigationToScreen = { nav.navigateSingleTopTo(item.destination) }
                    onNavigationToScreen = { nav.navigate(item.destination) }
                )
            }
        }
    }
}


