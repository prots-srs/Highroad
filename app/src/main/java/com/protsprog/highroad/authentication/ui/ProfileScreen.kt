package com.protsprog.highroad.authentication.ui

/*
TO READ
https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary
https://developer.android.com/reference/kotlin/androidx/compose/material/pullrefresh/package-summary

https://developer.android.com/jetpack/compose/layouts/material
https://developer.android.com/jetpack/compose/designsystems/material3#typography
 */
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.UserUIState
import com.protsprog.highroad.authentication.ui.theme.AuthTheme
import com.protsprog.highroad.ui.components.AppBar

private val layoutStep = 8.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    user: UserUIState,
    onNavigateUp: () -> Unit = {},
    onClickEdit: () -> Unit = {},
    sendRequest: Boolean = false,
    onSwipeUpdateData: () -> Unit = {}
) {
    val refreshState =
        rememberPullRefreshState(refreshing = sendRequest, onRefresh = onSwipeUpdateData)

    Scaffold(
        topBar = {
            AppBar(
                title = "User`s profile",
                onBackPressed = onNavigateUp
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onClickEdit,
                icon = { Icon(Icons.Outlined.Edit, "Edit") },
                text = { Text(text = "Edit profile") },
            )
        }
    ) { innerPadding ->

        Box(
            Modifier
                .pullRefresh(refreshState)
        ) {
            LazyColumn(
                modifier = modifier
                    .padding(innerPadding)
                    .padding(all = layoutStep * 4)
                    .fillMaxSize()
            ) {
//                if (!sendRequest) {
                    item() {
                        InfoRow(label = "Name", text = user.name)
                    }
                    item() {
                        Spacer(modifier.height(layoutStep * 3))
                        InfoRow(label = "Email", text = user.email)
                    }
//                }
            }

            PullRefreshIndicator(sendRequest, refreshState, Modifier.align(Alignment.TopCenter))
        }

        if (sendRequest) {
            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = modifier.width(layoutStep * 6),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    trackColor = MaterialTheme.colorScheme.secondary,
                )
            }
        }
    }
}

@Preview(device = "id:pixel_5", showBackground = true, backgroundColor = 0xFFA091AF)
@Composable
fun ProfileScreenPrevie() {
    AuthTheme {
        ProfileScreen(
            user = UserUIState(
                name = "Serhii",
                email = "my@internet.com"
            )
        )
    }
}


@Composable
fun InfoRow(
    modifier: Modifier = Modifier,
    label: String,
    text: String
) {
    Column {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )
        Spacer(modifier.height(layoutStep))
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Preview(device = "id:pixel_5", showBackground = true, backgroundColor = 0xFFF0F0FF)
@Composable
fun InfoRowPrevie() {
    AuthTheme {
        InfoRow(label = "Title row", text = "user@email")
    }
}