package com.protsprog.highroad.ui.components

import androidx.compose.foundation.layout.width
import androidx.compose.material.TopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

data class ItemMenu(
    val label: String,
    val action: () -> Unit
)

@Composable
fun AppBarMenu(
    title: String,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    optionMenu: List<ItemMenu> = emptyList()
) {
    var openMenu by remember { mutableStateOf(false) }

    TopAppBar(
        modifier = modifier,
        backgroundColor = MaterialTheme.colorScheme.surface,
        title = { AppBarTitle(title) },
        navigationIcon = { AppBarBackIcon(onBackPressed) },
        actions = {
            if (optionMenu.isNotEmpty()) {
                AppBarMenuIcon({ openMenu = !openMenu })
                DropdownMenu(
                    modifier = Modifier.width(width = 280.dp),
                    expanded = openMenu,
                    onDismissRequest = { openMenu = false },
                    offset = DpOffset(x = 8.dp, y = 0.dp),
                ) {
                    optionMenu.forEach {
                        DropdownMenuItem(
                            onClick = {
                                openMenu = false
                                it.action()
                            },
                            text = { AppBarMenuTitle(it.label) },
                        )

                    }
                }
            }
        }
    )
}

@Preview(device = "id:pixel_4")
@Composable
fun AppBarMenuPreview() {
//    EntranceTheme {
    AppBarMenu(
        title = "test app bal to cake invinsable",
        onBackPressed = {},
        optionMenu = listOf(
            ItemMenu(
                label = "abc kkrng",
                action = {}
            ),
            ItemMenu(
                label = "poling diolls",
                action = {}
            ),
            ItemMenu(
                label = "dommeux zillos",
                action = {}
            )
        )
    )
//    }
}