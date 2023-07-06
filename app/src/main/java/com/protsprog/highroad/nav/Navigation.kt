package com.protsprog.highroad.nav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.protsprog.highroad.CommonViewModel
import com.protsprog.highroad.R
import com.protsprog.highroad.articles.ArticleScreen
import com.protsprog.highroad.compose.ComposeScreen
import com.protsprog.highroad.compose.accessibility.data.AppJetNewsContainer
import com.protsprog.highroad.compose.animating.ui.Home
import com.protsprog.highroad.compose.basiclayouts.BasicLayoutsApp
import com.protsprog.highroad.compose.composePathways
import com.protsprog.highroad.compose.introduce.LessonApp
import com.protsprog.highroad.compose.navigation.ui.accounts.AccountsScreen
import com.protsprog.highroad.compose.navigation.ui.accounts.SingleAccountScreen
import com.protsprog.highroad.compose.navigation.ui.bills.BillsScreen
import com.protsprog.highroad.compose.navigation.ui.components.RallyTopAppBar
import com.protsprog.highroad.compose.navigation.ui.overview.OverviewScreen
import com.protsprog.highroad.compose.sideeffects.MainScreen
import com.protsprog.highroad.compose.sideeffects.details.DetailScreenLaunch
import com.protsprog.highroad.compose.states.WellnessScreen
import com.protsprog.highroad.compose.theming.ui.ThemingReplyApp
import com.protsprog.highroad.entrance.EntranceScreen
import com.protsprog.highroad.entrance.entranceItems
import com.protsprog.highroad.tictactoe.TicTacToeScreen
import com.protsprog.highroad.ui.components.AppBar
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.protsprog.highroad.compose.accessibility.ui.AppDrawer
import com.protsprog.highroad.compose.accessibility.ui.article.JetNewsArticleScreen
import com.protsprog.highroad.compose.accessibility.ui.home.HomeScreen
import com.protsprog.highroad.compose.accessibility.ui.home.JetNewsHomeScreen
import com.protsprog.highroad.compose.accessibility.ui.interests.InterestsScreen
import com.protsprog.highroad.compose.accessibility.ui.interests.JetNewsInterestsScreen
import com.protsprog.highroad.nav.MainJetNewsDestinations.ARTICLE_ID_KEY
import kotlinx.coroutines.launch

@Composable
fun HighroadNavigation(
    appJetNewsContainer: AppJetNewsContainer,
    windowWidthClass: WindowWidthSizeClass,
    viewModel: NavigationViewModel = viewModel()
) {
    val navController: NavHostController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    /*
    or
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination

        // Change the variable to this and use Overview as a backup screen if this returns null
        val currentScreen = rallyTabRowScreens.find { it.route == currentDestination?.route } ?: Overview
     */

    NavigationThemeSwitcher(typeAppTheme = viewModel.uiState.typeAppTheme) {
//        ????
//        val systemUiController = rememberSystemUiController()
//        SideEffect {
//            systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = false)
//        }

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                when (viewModel.uiState.typeAppTopBar) {
                    TYPE_TOPBAR.MAIN -> {
                        AppBar(
                            title = viewModel.uiState.currentScreenTitle,
                            onBackPressed = { navController.navigateUp() }
                        )
                    }

                    TYPE_TOPBAR.RALLY -> {
                        RallyTopAppBar(
                            allScreens = rallyTabRowScreens,
                            onTabSelected = { route ->
                                navController.navigateSingleTopTo(route)
                            },
                            currentScreenRoute = viewModel.uiState.currentScreenRoute
                        )
                    }

                    else -> {}
                }
            },
            drawerContent = {
                when (viewModel.uiState.typeAppTopBar) {
                    TYPE_TOPBAR.JETNEWS -> {
                        val currentJetNewsRoute = navBackStackEntry?.destination?.route ?: MainJetNewsDestinations.HOME_ROUTE

                        AppDrawer(
                            currentRoute = currentJetNewsRoute,
                            navigateToHome = { navController.navigate(MainJetNewsDestinations.HOME_ROUTE) },
                            navigateToInterests = { navController.navigate(MainJetNewsDestinations.INTERESTS_ROUTE) },
                            closeDrawer = { coroutineScope.launch { scaffoldState.drawerState.close() } }
                        )
                    }
                    else -> {
                    }
                }
            }
        ) { innerPadding ->
            HighroadNavHost(
                appJetNewsContainer = appJetNewsContainer,
                windowWidthClass = windowWidthClass,
                innerPadding = innerPadding,
                navController = navController,
                state = { config -> viewModel.configStates(config) },
                scaffoldState = scaffoldState
            )
        }
    }
}

@Composable
fun HighroadNavHost(
    appJetNewsContainer: AppJetNewsContainer,
    windowWidthClass: WindowWidthSizeClass,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    state: (CONFIGS_STATES) -> Unit,
    innerPadding: PaddingValues,
    scaffoldState: ScaffoldState
) {
    val actionsJetNews = remember(navController) { MainJetNewsActions(navController) }

    val coroutineScope = rememberCoroutineScope()
    val openDrawer: () -> Unit = { coroutineScope.launch { scaffoldState.drawerState.open() } }

    NavHost(
        navController = navController,
        startDestination = Entrance.route,
        modifier = modifier.padding(innerPadding)
    ) {
        composable(route = Entrance.route) {
            state(CONFIGS_STATES.ENTRANCE)

            EntranceScreen(
                windowWidthClass = windowWidthClass,
                navigations = entranceItems.map {
                    it.destination to {
                        navController.navigate(it.destination)
                    }
                }.toMap()
            )
        }
        composable(route = Compose.route) {
            state(CONFIGS_STATES.COMPOSE)

            ComposeScreen(
                navigations = composePathways.map {
                    it.caseId to {
                        when (it.caseId) {
                            7 -> navController.navigate(route = RallyOverview.route)
                            8 -> navController.navigate(route = MainJetNewsDestinations.HOME_ROUTE)
                            else -> navController.navigate(route = "${Compose.route}/${it.caseId}")
                        }
                    }
                }.toMap()
            )
        }
        composable(
            route = ComposeCase.routeWithArgs,
            arguments = ComposeCase.arguments
        ) { backStackEntry ->
            val caseId = backStackEntry?.let {
                it.arguments?.let {
                    it.getInt("case_id", 1) ?: 1
                }
            }
            when (caseId) {
                1 -> state(CONFIGS_STATES.COMPOSE_CASE_1)
                2 -> state(CONFIGS_STATES.COMPOSE_CASE_2)
                3 -> state(CONFIGS_STATES.COMPOSE_CASE_3)
                4 -> state(CONFIGS_STATES.COMPOSE_CASE_4)
                5 -> state(CONFIGS_STATES.COMPOSE_CASE_5)
                6 -> state(CONFIGS_STATES.COMPOSE_CASE_6)
                else -> state(CONFIGS_STATES.COMPOSE_CASE)
            }

            when (caseId) {
                1 -> LessonApp(Modifier.fillMaxSize())
                2 -> BasicLayoutsApp()
                3 -> WellnessScreen(Modifier.fillMaxSize())
                4 -> {
                    val viewModel: CommonViewModel = viewModel()
                    val uiState by viewModel.uiState.collectAsState()
                    androidx.compose.material3.Surface(tonalElevation = 5.dp) {
                        ThemingReplyApp(
                            replyHomeUIState = uiState,
                            closeDetailScreen = {
                                viewModel.closeDetailScreen()
                            },
                            navigateToDetail = { emailId ->
                                viewModel.setSelectedEmail(emailId)
                            }
                        )
                    }
                }

                5 -> Home()

//                Crane
                6 -> MainScreen(
                    onExploreItemClicked = { cityName ->
                        navController.navigate("${ComposeCaseMap.route}/${cityName}")
                    }
                )
                else -> {}
            }
        }
//        Crane detail
        composable(
            route = ComposeCaseMap.routeWithArgs,
            arguments = ComposeCaseMap.arguments
        ) { navBackStackEntry ->
            state(CONFIGS_STATES.COMPOSE_CASE_MAP)

            val cityName =
                navBackStackEntry.arguments?.getString(ComposeCaseMap.accountTypeArg) ?: ""

            DetailScreenLaunch(
                cityName = cityName,
                modifier = Modifier.padding(innerPadding)
            )
        }

//        RALLY
        composable(route = RallyOverview.route) {
            state(CONFIGS_STATES.COMPOSE_RALLY_OVERVIEW)

            Box(Modifier.padding(innerPadding)) {
                OverviewScreen(
                    onClickSeeAllAccounts = {
                        navController.navigate(RallyAccounts.route)
                    },
                    onClickSeeAllBills = {
                        navController.navigate(RallyBills.route)
                    },
                    onAccountClick = { accountType ->
                        navController.navigateToSingleAccount(accountType)
                    }
                )
            }
        }
        composable(route = RallyAccounts.route) {
            state(CONFIGS_STATES.COMPOSE_RALLY_ACCOUNTS)

            Box(Modifier.padding(innerPadding)) {
                AccountsScreen(
                    onAccountClick = { accountType ->
                        navController.navigateToSingleAccount(accountType)
                    }
                )
            }
        }
        composable(route = RallyBills.route) {
            state(CONFIGS_STATES.COMPOSE_RALLY_BILLS)

            Box(Modifier.padding(innerPadding)) {
                BillsScreen()
            }
        }
        composable(
            route = RallySingleAccount.routeWithArgs,
            arguments = RallySingleAccount.arguments,
            deepLinks = RallySingleAccount.deepLinks
        ) { navBackStackEntry ->
            state(CONFIGS_STATES.COMPOSE_RALLY_SINGLE_ACCOUNT)

            val accountType =
                navBackStackEntry.arguments?.getString(RallySingleAccount.accountTypeArg)

            Box(Modifier.padding(innerPadding)) {
                SingleAccountScreen(accountType)
            }
        }

//        JetNews
        composable(MainJetNewsDestinations.HOME_ROUTE) {
            state(CONFIGS_STATES.COMPOSE_CASE_JETNEWS_HOME)

            JetNewsHomeScreen(
                postsRepository = appJetNewsContainer.postsRepository,
                navigateToArticle = actionsJetNews.navigateToArticle,
                openDrawer = openDrawer,
            )
        }
        composable(MainJetNewsDestinations.INTERESTS_ROUTE) {
            state(CONFIGS_STATES.COMPOSE_CASE_JETNEWS_INTERESTS)

            JetNewsInterestsScreen(
                interestsRepository = appJetNewsContainer.interestsRepository,
                openDrawer = openDrawer,
            )
        }
        composable("${MainJetNewsDestinations.ARTICLE_ROUTE}/{$ARTICLE_ID_KEY}") { backStackEntry ->
            state(CONFIGS_STATES.COMPOSE_CASE_JETNEWS_ARTICLE)

            JetNewsArticleScreen(
                postId = backStackEntry.arguments?.getString(ARTICLE_ID_KEY),
                onBack = actionsJetNews.upPress,
                postsRepository = appJetNewsContainer.postsRepository
            )
        }

//        Articles
//                adb shell am start -a android.intent.action.VIEW -d "highroad://articles"
        composable(
            route = Articles.route,
            deepLinks = listOf(navDeepLink {
                uriPattern = "highroad://${Articles.route}"
            })
        ) {
            state(CONFIGS_STATES.ARTICLES)
            ArticleScreen(windowWidthClass = windowWidthClass)
        }

//        TicTacToe
        composable(route = TicTacToe.route) {
            state(CONFIGS_STATES.TICTACTOE)
            TicTacToeScreen()
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
//            this@navigateSingleTopTo.graph.findStartDestination().id
            Compose.route
        ) {
            saveState = true
        }
//        restoreState = true
        launchSingleTop = true
    }

private fun NavHostController.navigateToSingleAccount(accountType: String) {
    this.navigate("${RallySingleAccount.route}/$accountType")
}