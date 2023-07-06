package com.protsprog.highroad.entrance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.entrance.ui.components.EntranceCardHorizontal
import com.protsprog.highroad.entrance.ui.components.EntranceCardVertical

@Composable
fun EntranceScreen(
    windowWidthClass: WindowWidthSizeClass,
    navigations: Map<String, () -> Unit>
) {
    val verticalView =
        remember { derivedStateOf { windowWidthClass == WindowWidthSizeClass.Compact } }
    val space = if (verticalView.value) 16.dp else 24.dp
    LazyColumn(
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


