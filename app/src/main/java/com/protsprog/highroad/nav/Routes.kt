package com.protsprog.highroad.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.protsprog.highroad.articles.ui.ArticlesScreen
import com.protsprog.highroad.compose.ui.ComposeScreen
import com.protsprog.highroad.entrance.ui.EntranceScreen
import com.protsprog.highroad.tictactoe.ui.TicTacToeScreen

interface HighroadDestination {
    val route: String
    val screen: @Composable (nav: NavHostController) -> Unit
}

object Entrance : HighroadDestination {
    override val route = "entrance"
    override val screen: @Composable (nav: NavHostController) -> Unit = {nav -> EntranceScreen(nav)}
}

object Compose : HighroadDestination {
    override val route = "compose"
    override val screen: @Composable (nav: NavHostController) -> Unit = { nav -> ComposeScreen(nav) }
}

object ComposeCase : HighroadDestination {
    override val route = "compose/{caseId}"
    override val screen: @Composable (nav: NavHostController) -> Unit = { }
}

object Articles : HighroadDestination {
    override val route = "articles"
    override val screen: @Composable (nav: NavHostController) -> Unit = { nav -> ArticlesScreen() }
}

object TicTacToe : HighroadDestination {
    override val route = "tictactoe"
    override val screen: @Composable (nav: NavHostController) -> Unit = { nav -> TicTacToeScreen() }
}
