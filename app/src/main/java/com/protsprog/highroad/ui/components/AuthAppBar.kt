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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.protsprog.highroad.R
import com.protsprog.highroad.entrance.ui.theme.EntranceTheme

private val iconSize = 28.dp
private val layoutStep = 8.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthAppBar(
    hasBack: Boolean,
    hasAuth: Boolean = false,
    userName: String = "",
    userEmail: String = "",
    scrollBehavior: TopAppBarScrollBehavior,
    title: String,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    onClickLogin: () -> Unit = {},
    onClickProfile: () -> Unit = {},
    onClickLogout: () -> Unit = {}
) {
    var openUserMenu by remember { mutableStateOf(false) }

    TopAppBar(title = {
        Text(
            text = title, maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 18.sp
        )
    }, navigationIcon = {
        if (hasBack) {
            IconButton(
                onClick = onBackPressed,
                modifier = Modifier.padding(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.theming_back_button),
                    modifier = Modifier.size(iconSize)
                )
            }
        }
    }, actions = {
        if (hasAuth) {
            IconButton(
                onClick = { openUserMenu = !openUserMenu },
                modifier = Modifier.padding(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = stringResource(R.string.auth_show_user_menu),
                    modifier = Modifier.size(iconSize)
                )
            }

            DropdownMenu(
                modifier = Modifier.width(width = 250.dp),
                expanded = openUserMenu,
                onDismissRequest = {
                    openUserMenu = false
                },
                offset = DpOffset(x = 20.dp, y = (-10).dp),
            ) {

                DropdownMenuItem(
                    onClick = {},
                    text = {
                        Text(
                            text = userName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                )

                DropdownMenuItem(
                    onClick = {},
                    text = {
                        Text(
                            text = userEmail,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                )

                DropdownMenuItem(onClick = onClickProfile, text = {
                    Text(
                        text = "View profile",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }, trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = null,
                        modifier = modifier.size(20.dp)
                    )
                })

                DropdownMenuItem(text = {
                    Text(
                        text = "Sign out",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }, onClick = onClickLogout, trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Logout,
                        contentDescription = null,
                        modifier = modifier.size(20.dp)
                    )
                })
            }
        } else {
            IconButton(
                onClick = onClickLogin,
                modifier = Modifier.padding(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Input,
                    contentDescription = stringResource(R.string.auth_sign_in),
                    modifier = Modifier.size(iconSize)
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
    EntranceTheme {
        AuthAppBar(
            hasBack = false,
            title = "ABC application",
            hasAuth = true,
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        )
    }
}

