package com.protsprog.highroad.ui.components

/*
TO READ

https://developer.android.com/jetpack/compose/components/app-bars#center

https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#TopAppBar(kotlin.Function0,androidx.compose.ui.Modifier,kotlin.Function0,kotlin.Function1,androidx.compose.foundation.layout.WindowInsets,androidx.compose.material3.TopAppBarColors,androidx.compose.material3.TopAppBarScrollBehavior)

https://m3.material.io/components/top-app-bar/overview
https://m3.material.io/components/top-app-bar/guidelines#b1b64842-7d88-4c3f-8ffb-4183fe648c9e
*/
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Input
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.protsprog.highroad.R
import com.protsprog.highroad.articles.ui.theme.ArticlesTheme
import com.protsprog.highroad.authentication.AuthServices
import com.protsprog.highroad.authentication.StateActionsAuthTopBar

private val iconSize = 28.dp

enum class TOP_BAR_MENU_ACTIONS { RELOAD, DELETE }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthAppBar(
    hasBack: Boolean,
    authService: AuthServices,
    scrollBehavior: TopAppBarScrollBehavior,
    title: String,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    actions: Map<TOP_BAR_MENU_ACTIONS, () -> Unit> = emptyMap()
) {
    var openUserMenu by remember { mutableStateOf(false) }

    val iconModifier = modifier.size(iconSize)
    val iconButtonModifier = modifier.padding(
        start = 0.dp,
        top = 0.dp,
        bottom = 0.dp,
        end = dimensionResource(id = R.dimen.padding_small)
    )

    TopAppBar(title = {
        Text(
            text = title, maxLines = 2, overflow = TextOverflow.Ellipsis, fontSize = 18.sp
        )
    }, navigationIcon = {
        if (hasBack) {
            IconButton(
                onClick = onBackPressed,
                modifier = iconButtonModifier,
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.theming_back_button),
                    modifier = iconModifier
                )
            }
        }
    }, actions = {
        actions.forEach { action ->
            IconButton(
                onClick = action.value,
                modifier = iconButtonModifier,
            ) {
                when (action.key) {
                    TOP_BAR_MENU_ACTIONS.RELOAD -> Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Reload",
                        modifier = iconModifier
                    )
                    TOP_BAR_MENU_ACTIONS.DELETE -> Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        modifier = iconModifier
                    )
                }
            }
        }
        if (authService.hasAuthorization) {
            IconButton(
                onClick = { openUserMenu = !openUserMenu },
                modifier = iconButtonModifier,
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = stringResource(R.string.auth_show_user_menu),
                    modifier = iconModifier
                )
            }

            DropdownMenu(
                modifier = Modifier.width(width = 280.dp),
                expanded = openUserMenu,
                onDismissRequest = {
                    openUserMenu = false
                },
                offset = DpOffset(x = 8.dp, y = (-4).dp),
            ) {

                DropDownItemInMenu(label = authService.name)
                DropDownItemInMenu(label = authService.email)
                DropDownItemInMenu(
                    label = "View profile",
                    onClick = authService.onClickProfile,
                    icon = Icons.Outlined.Settings
                )
                DropDownItemInMenu(
                    label = "Sign out",
                    onClick = authService.onClickLogout,
                    icon = Icons.Outlined.Logout
                )
            }
        } else {
            IconButton(
                onClick = authService.onClickLogin,
                modifier = iconButtonModifier,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Input,
                    contentDescription = stringResource(R.string.auth_sign_in),
                    modifier = iconModifier
                )
            }
        }
    }, scrollBehavior = scrollBehavior, colors = topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        titleContentColor = MaterialTheme.colorScheme.onPrimary,
        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
        scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer
    )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun NoAuthAuthAppBarPreview() {
    ArticlesTheme {
        AuthAppBar(
            hasBack = true,
            title = "ABC application",
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            actions = mapOf(TOP_BAR_MENU_ACTIONS.RELOAD to {}),
            authService = StateActionsAuthTopBar(
                name = "abc",
                email = "sd@sedf",
                hasAuthorization = false,
                onClickLogin = {},
                onClickLogout = {},
                onClickProfile = {}
            )
        )
    }
}

@Composable
fun DropDownItemInMenu(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit = {},
    icon: ImageVector? = null
) {
    DropdownMenuItem(
//        modifier = modifier.,
        onClick = onClick,
        text = {
            Text(
                text = label,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        trailingIcon = {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = modifier.size(iconSize)
                )
            }
        }
    )
}
