package com.protsprog.highroad.nav

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.protsprog.highroad.compose.composePathways
import com.protsprog.highroad.compose.theming.ui.ReplyHomeViewModel
import com.protsprog.highroad.compose.ui.ComposeCase
import com.protsprog.highroad.compose.ui.ComposeScreen
import com.protsprog.highroad.entrance.ui.EntranceScreen
import com.protsprog.highroad.ui.components.AppBar
import androidx.activity.viewModels

@Composable
fun HighroadNavigation(replyViewModel: ReplyHomeViewModel) {
    var currentScreen: HighroadDestination by remember { mutableStateOf(Entrance) }
    var screenTitle: String by remember { mutableStateOf("") }
    val navController = rememberNavController()

//    val currentBackStack by navController.currentBackStackEntryAsState()

    Scaffold(
        topBar = {
            AppBar(
                title = screenTitle,
                modifier = Modifier,
                onBackPressed = { screen ->
                    currentScreen = screen
                    navController.navigateUp()
                },
//                currentScreen = currentBackStack?.destination.
                currentScreen = currentScreen
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Entrance.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Entrance.route) {
                currentScreen = Entrance
                Entrance.screen(navController)
            }
            composable(route = Compose.route) {
                currentScreen = Compose
                screenTitle = "Compose case"
                Compose.screen(navController)
            }
            composable(
                route = ComposeCase.route,
                arguments = listOf(navArgument("caseId") { type = NavType.IntType })
            ) { backStackEntry ->

                val needId = backStackEntry?.arguments?.getInt("caseId", 1) ?: 1
                currentScreen = ComposeCase
                screenTitle = composePathways.find {
                    it.caseId == needId
                }?.title ?: ""
                ComposeCase(needId, replyViewModel)
            }
            composable(route = Articles.route) {
                currentScreen = Articles
                screenTitle = "Articles case"
                Articles.screen(navController)
            }
            composable(route = TicTacToe.route) {
                currentScreen = TicTacToe
                screenTitle = "TicTacToe case"
                TicTacToe.screen(navController)
            }
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { launchSingleTop = true }