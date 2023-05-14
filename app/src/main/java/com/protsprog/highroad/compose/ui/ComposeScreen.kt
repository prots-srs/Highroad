package com.protsprog.highroad.compose.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.protsprog.highroad.compose.ComposePathway
import com.protsprog.highroad.compose.basiclayouts.BasicLayoutsApp
import com.protsprog.highroad.compose.composePathways
import com.protsprog.highroad.compose.introduce.IntroduceApp
import com.protsprog.highroad.compose.states.StatesApp
import com.protsprog.highroad.compose.theming.ui.ReplyHomeViewModel
import com.protsprog.highroad.compose.theming.ui.ThemingReplyApp
import com.protsprog.highroad.compose.theming.ui.theme.ThemingTheme
import com.protsprog.highroad.entrance.ui.theme.EntranceTheme
import com.protsprog.highroad.nav.Compose

@Composable
fun ComposeScreen(nav: NavHostController) {
    EntranceTheme {
        LazyColumn {
            items(composePathways) { item ->
                PathwayOption(
                    item = item,
                    onNavigationToScreen = { nav.navigate(Compose.route + "/" + item.caseId) }
                )
            }
        }
        Spacer(Modifier.size(16.dp))
    }
}

@Composable
fun ComposeCase(caseId: Int, replyViewModel: ReplyHomeViewModel) {
    when (caseId) {
        1 ->
            IntroduceApp()

        2 ->
            BasicLayoutsApp()

        3 ->
            StatesApp()

        4 -> {
            val uiState by replyViewModel.uiState.collectAsState()
            ThemingTheme {
                Surface(tonalElevation = 5.dp) {
                    ThemingReplyApp(
                        replyHomeUIState = uiState,
                        closeDetailScreen = {
                            replyViewModel.closeDetailScreen()
                        },
                        navigateToDetail = { emailId ->
                            replyViewModel.setSelectedEmail(emailId)
                        }
                    )
                }
            }
        }

        else -> {}
    }
}

@Composable
fun PathwayOption(
    item: ComposePathway,
    onNavigationToScreen: () -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .clickable(
                onClick = onNavigationToScreen
            ),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.primary
    ) {
        Text(
            text = item.title,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(all = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PathwayOptionPreview() {
    EntranceTheme {
        PathwayOption(
            item = composePathways[0],
            onNavigationToScreen = {}
        )
    }
}