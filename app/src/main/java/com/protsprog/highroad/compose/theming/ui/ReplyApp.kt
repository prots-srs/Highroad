package com.protsprog.highroad.compose.theming.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuOpen
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.reply.ui.ReplyDestinations
import com.example.reply.ui.ReplyListAndDetailContent
import com.example.reply.ui.ReplyListOnlyContent
import com.protsprog.highroad.R
import com.protsprog.highroad.compose.theming.ReplyHomeUIState
import com.protsprog.highroad.util.ContentType
import com.protsprog.highroad.util.DevicePosture
import com.protsprog.highroad.util.NavigationType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplyApp(
    windowSize: WindowWidthSizeClass,
    foldingDevicePosture: DevicePosture,
    replyHomeUIState: ReplyHomeUIState
) {
    /**
     * This will help us select type of navigation and content type depending on window size and
     * fold state of the device.
     */
    val navigationType: NavigationType
    val contentType: ContentType

    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            navigationType = NavigationType.BOTTOM_NAVIGATION
            contentType = ContentType.LIST_ONLY
        }

        WindowWidthSizeClass.Medium -> {
            navigationType = NavigationType.NAVIGATION_RAIL
            contentType = if (foldingDevicePosture is DevicePosture.BookPosture
                || foldingDevicePosture is DevicePosture.Separating
            ) {
                ContentType.LIST_AND_DETAIL
            } else {
                ContentType.LIST_ONLY
            }
        }

        WindowWidthSizeClass.Expanded -> {
            navigationType = if (foldingDevicePosture is DevicePosture.BookPosture) {
                NavigationType.NAVIGATION_RAIL
            } else {
                NavigationType.PERMANENT_NAVIGATION_DRAWER
            }
            contentType = ContentType.LIST_AND_DETAIL
        }

        else -> {
            navigationType = NavigationType.BOTTOM_NAVIGATION
            contentType = ContentType.LIST_ONLY
        }
    }

    ReplyNavigationWrapperUI(
        navigationType = navigationType,
        replyHomeUIState = replyHomeUIState,
        contentType = contentType
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReplyNavigationWrapperUI(
    navigationType: NavigationType,
    contentType: ContentType,
    replyHomeUIState: ReplyHomeUIState
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedDestination = ReplyDestinations.INBOX

    if (navigationType == NavigationType.PERMANENT_NAVIGATION_DRAWER) {
        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet {
                    NavigationDrawerContent(selectedDestination)
                }
            }
        ) {
            ReplyAppContent(navigationType, contentType, replyHomeUIState)
        }
    } else {
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    NavigationDrawerContent(
                        selectedDestination,
                        onDrawerClicked = {
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    )
                }
            },
            drawerState = drawerState
        ) {
            ReplyAppContent(
                navigationType, contentType, replyHomeUIState,
                onDrawerClicked = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
        }
    }
}


@Composable
fun ReplyAppContent(
    navigationType: NavigationType,
    contentType: ContentType,
    replyHomeUIState: ReplyHomeUIState,
    onDrawerClicked: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AnimatedVisibility(visible = navigationType == NavigationType.NAVIGATION_RAIL) {
            ReplyNavigationRail(
                onDrawerClicked = onDrawerClicked
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {

            if (contentType == ContentType.LIST_AND_DETAIL) {
                ReplyListAndDetailContent(
                    replyHomeUIState = replyHomeUIState,
                    modifier = Modifier.weight(1f),
                )
            } else {
                ReplyListOnlyContent(replyHomeUIState = replyHomeUIState, modifier = Modifier.weight(1f))
            }

            AnimatedVisibility(visible = navigationType == NavigationType.BOTTOM_NAVIGATION) {
                ReplyBottomNavigationBar()
            }
        }
    }
}

@Composable
@Preview
fun ReplyNavigationRail(
    onDrawerClicked: () -> Unit = {},
) {
    NavigationRail(modifier = Modifier.fillMaxHeight()) {
        NavigationRailItem(
            selected = false,
            onClick = onDrawerClicked,
            icon = {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(id = R.string.reply_navigation_drawer)
                )
            }
        )
        NavigationRailItem(
            selected = true,
            onClick = { /*TODO*/ },
            icon = {
                Icon(
                    imageVector = Icons.Default.Inbox,
                    contentDescription = stringResource(id = R.string.reply_tab_inbox)
                )
            }
        )
        NavigationRailItem(
            selected = false,
            onClick = {/*TODO*/ },
            icon = {
                Icon(
                    imageVector = Icons.Default.Article,
                    stringResource(id = R.string.reply_tab_article)
                )
            }
        )
        NavigationRailItem(
            selected = false,
            onClick = { /*TODO*/ },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Chat,
                    stringResource(id = R.string.reply_tab_dm)
                )
            }
        )
        NavigationRailItem(
            selected = false,
            onClick = { /*TODO*/ },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.People,
                    stringResource(id = R.string.reply_tab_groups)
                )
            }
        )
    }
}

@Composable
@Preview
fun ReplyBottomNavigationBar() {
    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        NavigationBarItem(
            selected = true,
            onClick = { /*TODO*/ },
            icon = {
                Icon(
                    imageVector = Icons.Default.Inbox,
                    contentDescription = stringResource(id = R.string.reply_tab_inbox)
                )
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /*TODO*/ },
            icon = {
                Icon(
                    imageVector = Icons.Default.Article,
                    contentDescription = stringResource(id = R.string.reply_tab_inbox)
                )
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /*TODO*/ },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Chat,
                    contentDescription = stringResource(id = R.string.reply_tab_inbox)
                )
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /*TODO*/ },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Videocam,
                    contentDescription = stringResource(id = R.string.reply_tab_inbox)
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawerContent(
    selectedDestination: String,
    modifier: Modifier = Modifier,
    onDrawerClicked: () -> Unit = {}
) {
    Column(
        modifier
            .wrapContentWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .padding(24.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.app_name).uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            IconButton(onClick = onDrawerClicked) {
                Icon(
                    imageVector = Icons.Default.MenuOpen,
                    contentDescription = stringResource(id = R.string.reply_navigation_drawer)
                )
            }
        }

        NavigationDrawerItem(
            selected = selectedDestination == ReplyDestinations.INBOX,
            label = {
                Text(
                    text = stringResource(id = R.string.reply_tab_inbox),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Inbox,
                    contentDescription = stringResource(id = R.string.reply_tab_inbox)
                )
            },
            colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
            onClick = { /*TODO*/ }
        )
        NavigationDrawerItem(
            selected = selectedDestination == ReplyDestinations.ARTICLES,
            label = {
                Text(
                    text = stringResource(id = R.string.reply_tab_article),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Article,
                    contentDescription = stringResource(id = R.string.reply_tab_article)
                )
            },
            colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
            onClick = { /*TODO*/ }
        )
        NavigationDrawerItem(
            selected = selectedDestination == ReplyDestinations.DM,
            label = {
                Text(
                    text = stringResource(id = R.string.reply_tab_dm),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = stringResource(id = R.string.reply_tab_dm)
                )
            },
            colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
            onClick = { /*TODO*/ }
        )
        NavigationDrawerItem(
            selected = selectedDestination == ReplyDestinations.GROUPS,
            label = {
                Text(
                    text = stringResource(id = R.string.reply_tab_groups),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Article,
                    contentDescription = stringResource(id = R.string.reply_tab_groups)
                )
            },
            colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
            onClick = { /*TODO*/ }
        )
    }
}