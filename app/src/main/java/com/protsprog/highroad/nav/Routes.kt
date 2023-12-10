package com.protsprog.highroad.nav

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.protsprog.highroad.R

interface IconDestination {
    val icon: ImageVector?
    val title: String
    val route: String
}

/*
Inventory Application
 */
interface SimpleDestination {
    val route: String
    val titleRes: Int
}

object Entrance : IconDestination {
    override val icon = null
    override val title = "Highroad application codesamples"
    override val route = "entrance"
}

object Compose : IconDestination {
    override val icon = null
    override val title = "Compose cases"
    override val route = "compose"
}

object ComposeCase : IconDestination {
    override val icon = null
    override val title = ""
    override val route = "compose_case"
    const val accountTypeArg = "case_id"
    val routeWithArgs = "${Compose.route}/{${accountTypeArg}}"
    val arguments = listOf(
        navArgument(accountTypeArg) { type = NavType.IntType }
    )
}

object ComposeCaseMap : IconDestination {
    override val icon = null
    override val route = "compose_case_map"
    override val title = "City location"
    const val accountTypeArg = "city_name"
    val routeWithArgs = "${route}/{${accountTypeArg}}"
    val arguments = listOf(
        navArgument(accountTypeArg) { type = NavType.StringType }
    )
}

object Articles : SimpleDestination {
    override val titleRes = R.string.title_network_articles_case
    override val route = "network_articles"
    const val itemIdArg = "itemId"
    val routeWithArgs = "${route}/{${itemIdArg}}"
    val arguments = listOf(
        navArgument(itemIdArg) { type = NavType.IntType }
    )
    val routeEdit = "${route}/edit/{${itemIdArg}}"
}

class ArticlesActions(navController: NavHostController) {
    val navigateToArticle: (Int) -> Unit = { itemId ->
        navController.navigate(
            route = Articles.routeWithArgs.replace(
                "{${Articles.itemIdArg}}",
                itemId.toString()
            )
        )
    }
    val navigateToEdit: (Int) -> Unit = { itemId ->
        navController.navigate(
            route = Articles.routeEdit.replace(
                "{${Articles.itemIdArg}}",
                itemId.toString()
            )
        )
    }
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}

object TicTacToe : IconDestination {
    override val icon = null
    override val title = "TicTacToe case"
    override val route = "tictactoe"
}

object FlightSearch : IconDestination {
    override val icon = null
    override val title = "Flight Search"
    override val route = "flightsearch"
}

object MotionCase : IconDestination {
    override val icon = null
    override val title = "Motion case"
    override val route = "motioncase"
}

/**
 * Rally app navigation destinations
 */
object RallyOverview : IconDestination {
    override val icon = Icons.Filled.PieChart
    override val route = "compose_navigation_overview"
    override val title = "overview"
}

object RallyAccounts : IconDestination {
    override val icon = Icons.Filled.AttachMoney
    override val route = "compose_navigation_accounts"
    override val title = "accounts"
}

object RallyBills : IconDestination {
    override val icon = Icons.Filled.MoneyOff
    override val route = "compose_navigation_bills"
    override val title = "bills"
}

object RallySingleAccount : IconDestination {
    override val icon = Icons.Filled.Money
    override val title = "single_account"
    override val route = "compose_navigation_single_account"
    const val accountTypeArg = "account_type"
    val routeWithArgs = "${route}/{${accountTypeArg}}"
    val arguments = listOf(
        navArgument(accountTypeArg) { type = NavType.StringType }
    )
    val deepLinks = listOf(
        navDeepLink { uriPattern = "highroad://${route}/{${accountTypeArg}}" }
    )
}

// Screens to be displayed in the top RallyTabRow
val rallyTabRowScreens = listOf(RallyOverview, RallyAccounts, RallyBills)

/*
JetNews
 */
object MainJetNewsDestinations {
    const val HOME_ROUTE = "jetnews_home"
    const val INTERESTS_ROUTE = "jetnews_interests"
    const val ARTICLE_ROUTE = "jetnews_post"
    const val ARTICLE_ID_KEY = "jetnews_postId"
}

class MainJetNewsActions(navController: NavHostController) {
    val navigateToArticle: (String) -> Unit = { postId: String ->
        navController.navigate("${MainJetNewsDestinations.ARTICLE_ROUTE}/$postId")
    }
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}


object InventoryHomeDestination : SimpleDestination {
    override val route = "inventory_home"
    override val titleRes = R.string.inventory_app_name
}

object InventoryItemDetailsDestination : SimpleDestination {
    override val route = "inventory_item_details"
    override val titleRes = R.string.inventory_item_detail_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

object InventoryItemEntryDestination : SimpleDestination {
    override val route = "inventory_item_entry"
    override val titleRes = R.string.inventory_item_entry_title
}

object InventoryItemEditDestination : SimpleDestination {
    override val route = "inventory_item_edit"
    override val titleRes = R.string.inventory_edit_item_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

object ToDoCase : SimpleDestination {
    override val titleRes = R.string.entrance_todo_case_title
    override val route = "todocase"
}

object BluetoothCase : SimpleDestination {
    override val route = "bluetooth"
    override val titleRes = R.string.entrance_bluetooth
}

object CameraXCase : SimpleDestination {
    override val titleRes = R.string.entrance_camerax
    override val route = "cameraX"
}



/*
Bus Schedule app
 */
enum class BusScheduleDestinations {
    FullSchedule,
    RouteSchedule
}

/*
Authorization
 */
object AuthDestinations {
    const val loginPage = "AuthLogin"
    const val profilePage = "AuthProfile"
    const val profileEditPage = "AuthProfileEdit"
}