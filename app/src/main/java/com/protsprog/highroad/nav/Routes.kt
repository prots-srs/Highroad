package com.protsprog.highroad.nav

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.protsprog.highroad.R
import com.protsprog.highroad.articles.ArticleScreen
import com.protsprog.highroad.compose.ComposeScreen
import com.protsprog.highroad.compose.navigation.ui.accounts.AccountsScreen
import com.protsprog.highroad.compose.navigation.ui.accounts.SingleAccountScreen
import com.protsprog.highroad.compose.navigation.ui.bills.BillsScreen
import com.protsprog.highroad.compose.navigation.ui.overview.OverviewScreen
import com.protsprog.highroad.entrance.EntranceScreen
import com.protsprog.highroad.tictactoe.TicTacToeScreen

interface HighroadDestination {
    val icon: ImageVector?
    val title: String
    val route: String
}

object Entrance : HighroadDestination {
    override val icon = null
    override val title = "Highroad application codesamples"
    override val route = "entrance"
}

object Compose : HighroadDestination {
    override val icon = null
    override val title = "Compose cases"
    override val route = "compose"
}

object ComposeCase : HighroadDestination {
    override val icon = null
    override val title = ""
    override val route = "compose_case"
    const val accountTypeArg = "case_id"
    val routeWithArgs = "${Compose.route}/{${accountTypeArg}}"
    val arguments = listOf(
        navArgument(accountTypeArg) { type = NavType.IntType }
    )
}

object ComposeCaseMap : HighroadDestination {
    override val icon = null
    override val route = "compose_case_map"
    override val title = "City location"
    const val accountTypeArg = "city_name"
    val routeWithArgs = "${route}/{${accountTypeArg}}"
    val arguments = listOf(
        navArgument(accountTypeArg) { type = NavType.StringType }
    )
}

object Articles : HighroadDestination {
    override val icon = null
    override val title = "Network Articles case"
    override val route = "articles"
}

object TicTacToe : HighroadDestination {
    override val icon = null
    override val title = "TicTacToe case"
    override val route = "tictactoe"
}

object FlightSearch : HighroadDestination {
    override val icon = null
    override val title = "Flight Search"
    override val route = "flightsearch"
}

object MotionCase : HighroadDestination {
    override val icon = null
    override val title = "Motion case"
    override val route = "motioncase"
}

/**
 * Rally app navigation destinations
 */
object RallyOverview : HighroadDestination {
    override val icon = Icons.Filled.PieChart
    override val route = "compose_navigation_overview"
    override val title = "overview"
}

object RallyAccounts : HighroadDestination {
    override val icon = Icons.Filled.AttachMoney
    override val route = "compose_navigation_accounts"
    override val title = "accounts"
}

object RallyBills : HighroadDestination {
    override val icon = Icons.Filled.MoneyOff
    override val route = "compose_navigation_bills"
    override val title = "bills"
}

object RallySingleAccount : HighroadDestination {
    override val icon = Icons.Filled.Money
    override val title = "single_account"
    override val route = "compose_navigation_single_account"
    const val accountTypeArg = "account_type"
    val routeWithArgs = "${route}/{${accountTypeArg}}"
    val arguments = listOf(
        navArgument(accountTypeArg) { type = NavType.StringType }
    )
    val deepLinks = listOf(
        navDeepLink { uriPattern = "highroad://$route/{$accountTypeArg}" }
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

/*
Inventory Application
 */
interface InventoryDestination {
    val route: String
    val titleRes: Int
}

object InventoryHomeDestination : InventoryDestination {
    override val route = "inventory_home"
    override val titleRes = R.string.inventory_app_name
}

object InventoryItemDetailsDestination : InventoryDestination {
    override val route = "inventory_item_details"
    override val titleRes = R.string.inventory_item_detail_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

object InventoryItemEntryDestination : InventoryDestination {
    override val route = "inventory_item_entry"
    override val titleRes = R.string.inventory_item_entry_title
}

object InventoryItemEditDestination : InventoryDestination {
    override val route = "inventory_item_edit"
    override val titleRes = R.string.inventory_edit_item_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

/*
Bus Schedule app
 */
enum class BusScheduleDestinations {
    FullSchedule,
    RouteSchedule
}
