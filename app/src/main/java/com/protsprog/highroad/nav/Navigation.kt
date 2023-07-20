package com.protsprog.highroad.nav

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.protsprog.highroad.R
import com.protsprog.highroad.articles.ArticleScreen
import com.protsprog.highroad.articles.ui.theme.ArticlesTheme
import com.protsprog.highroad.compose.ComposeScreen
import com.protsprog.highroad.compose.accessibility.data.AppJetNewsContainer
import com.protsprog.highroad.compose.accessibility.ui.AppDrawer
import com.protsprog.highroad.compose.accessibility.ui.article.JetNewsArticleScreen
import com.protsprog.highroad.compose.accessibility.ui.home.JetNewsHomeScreen
import com.protsprog.highroad.compose.accessibility.ui.interests.JetNewsInterestsScreen
import com.protsprog.highroad.compose.accessibility.ui.theme.JetnewsTheme
import com.protsprog.highroad.compose.animating.ui.Home
import com.protsprog.highroad.compose.animating.ui.theme.AnimationCodelabTheme
import com.protsprog.highroad.compose.basiclayouts.BasicLayoutsApp
import com.protsprog.highroad.compose.basiclayouts.theme.BasicLayoutsTheme
import com.protsprog.highroad.compose.bus_schedule.ui.BusScheduleViewModel
import com.protsprog.highroad.compose.bus_schedule.ui.FullScheduleScreen
import com.protsprog.highroad.compose.bus_schedule.ui.RouteScheduleScreen
import com.protsprog.highroad.compose.bus_schedule.ui.components.BusScheduleTopAppBar
import com.protsprog.highroad.compose.bus_schedule.ui.theme.BusScheduleTheme
import com.protsprog.highroad.compose.composePathways
import com.protsprog.highroad.compose.introduce.LessonApp
import com.protsprog.highroad.compose.navigation.ui.accounts.AccountsScreen
import com.protsprog.highroad.compose.navigation.ui.accounts.SingleAccountScreen
import com.protsprog.highroad.compose.navigation.ui.bills.BillsScreen
import com.protsprog.highroad.compose.navigation.ui.components.RallyTopAppBar
import com.protsprog.highroad.compose.navigation.ui.overview.OverviewScreen
import com.protsprog.highroad.compose.navigation.ui.theme.RallyTheme
import com.protsprog.highroad.compose.persistroom.data.AppInventoryContainer
import com.protsprog.highroad.compose.persistroom.ui.home.InventoryHomeScreen
import com.protsprog.highroad.compose.persistroom.ui.item.InventoryItemDetailsScreen
import com.protsprog.highroad.compose.persistroom.ui.item.InventoryItemEditScreen
import com.protsprog.highroad.compose.persistroom.ui.item.InventoryItemEntryScreen
import com.protsprog.highroad.compose.persistroom.ui.theme.InventoryTheme
import com.protsprog.highroad.compose.sideeffects.MainScreen
import com.protsprog.highroad.compose.sideeffects.details.DetailScreenLaunch
import com.protsprog.highroad.compose.sideeffects.ui.CraneTheme
import com.protsprog.highroad.compose.states.WellnessScreen
import com.protsprog.highroad.compose.states.theme.StatesTheme
import com.protsprog.highroad.compose.theming.ReplyScreen
import com.protsprog.highroad.compose.theming.ui.theme.ReplyTheme
import com.protsprog.highroad.entrance.EntranceScreen
import com.protsprog.highroad.entrance.entranceItems
import com.protsprog.highroad.entrance.ui.theme.EntranceTheme
import com.protsprog.highroad.nav.MainJetNewsDestinations.ARTICLE_ID_KEY
import com.protsprog.highroad.tictactoe.TicTacToeScreen
import com.protsprog.highroad.tictactoe.ui.theme.TicTacToeTheme
import com.protsprog.highroad.ui.components.AppBar
import com.protsprog.highroad.ui.theme.IntroduceTheme
import com.protsprog.highroad.util.DevicePosture
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HighroadNavigation(
//    appJetNewsContainer: AppJetNewsContainer,
//    appInventoryContainer: AppInventoryContainer,
    windowWidthClass: WindowWidthSizeClass,
    devicePosture: DevicePosture
) {
    val navController: NavHostController = rememberNavController()
//    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

//    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var caseTheme by rememberSaveable { mutableStateOf<TYPE_THEME>(TYPE_THEME.MAIN) }

    NavigationThemeSwitcher(typeAppTheme = caseTheme) {

        NavHost(
            navController = navController,
            startDestination = Entrance.route,
            modifier = Modifier
        ) {
            composable(route = Entrance.route) {
                caseTheme = TYPE_THEME.MAIN
                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        TopAppBar(
                            modifier = Modifier,
                            backgroundColor = MaterialTheme.colorScheme.surface,
                            title = {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = Entrance.title,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.titleMedium,
                                    )
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    EntranceScreen(
                        modifier = Modifier.padding(innerPadding),
                        windowWidthClass = windowWidthClass,
                        navigations = entranceItems.map {
                            it.destination to {
                                navController.navigate(it.destination)
                            }
                        }.toMap()
                    )
                }
            }

            composable(route = Compose.route) {
                caseTheme = TYPE_THEME.MAIN
                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        AppBar(
                            title = Compose.title,
                            onBackPressed = { navController.navigateUp() }
                        )
                    }
                ) { innerPadding ->
                    ComposeScreen(
                        modifier = Modifier.padding(innerPadding),
                        navigations = composePathways.map {
                            it.caseId to {
                                when (it.caseId) {
                                    7 -> navController.navigate(route = RallyOverview.route)
                                    8 -> navController.navigate(route = MainJetNewsDestinations.HOME_ROUTE)
                                    9 -> navController.navigate(route = InventoryHomeDestination.route)
                                    10 -> navController.navigate(route = BusScheduleDestinations.FullSchedule.name)
                                    else -> navController.navigate(route = "${Compose.route}/${it.caseId}")
                                }
                            }
                        }.toMap()
                    )
                }
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
//                set theme
                when (caseId) {
                    1 -> caseTheme = TYPE_THEME.COMPOSE_INTRODUCE
                    2 -> caseTheme = TYPE_THEME.COMPOSE_BASICLAYOUT
                    3 -> caseTheme = TYPE_THEME.COMPOSE_STATES
                    4 -> caseTheme = TYPE_THEME.COMPOSE_THEMING
                    5 -> caseTheme = TYPE_THEME.COMPOSE_ANIMATION
                    6 -> caseTheme = TYPE_THEME.COMPOSE_CRANE
                    else -> caseTheme = TYPE_THEME.MAIN
                }

                when (caseId) {
                    1 -> LessonApp(Modifier.fillMaxSize())
                    2 -> BasicLayoutsApp()
                    3 -> WellnessScreen(Modifier.fillMaxSize())
                    4 -> ReplyScreen(
                        windowWidthClass = windowWidthClass,
                        foldingDevicePosture = devicePosture
                    )

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
                caseTheme = TYPE_THEME.COMPOSE_CRANE

                val cityName =
                    navBackStackEntry.arguments?.getString(ComposeCaseMap.accountTypeArg) ?: ""

                DetailScreenLaunch(cityName = cityName)
            }

//        Articles
//                adb shell am start -a android.intent.action.VIEW -d "highroad://articles"
            composable(
                route = Articles.route,
                deepLinks = listOf(navDeepLink {
                    uriPattern = "highroad://${Articles.route}"
                })
            ) {
                caseTheme = TYPE_THEME.ARTICLES
                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        AppBar(
                            title = Articles.title,
                            onBackPressed = { navController.navigateUp() }
                        )
                    }
                ) { innerPadding ->
                    ArticleScreen(
                        modifier = Modifier.padding(innerPadding),
                        windowWidthClass = windowWidthClass
                    )
                }
            }

//        TicTacToe
            composable(route = TicTacToe.route) {
                caseTheme = TYPE_THEME.TICTACTOE
                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        AppBar(
                            title = TicTacToe.title,
                            onBackPressed = { navController.navigateUp() }
                        )
                    }
                ) { innerPadding ->
                    TicTacToeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }

//            Inventory
            composable(route = InventoryHomeDestination.route) {
                caseTheme = TYPE_THEME.COMPOSE_INVENTORY
                InventoryHomeScreen(
                    navigateToItemEntry = { navController.navigate(InventoryItemEntryDestination.route) },
                    navigateToItemUpdate = {
                        navController.navigate("${InventoryItemDetailsDestination.route}/${it}")
                    }
                )
            }
            composable(route = InventoryItemEntryDestination.route) {
                caseTheme = TYPE_THEME.COMPOSE_INVENTORY
                InventoryItemEntryScreen(
                    navigateBack = { navController.popBackStack() },
                    onNavigateUp = { navController.navigateUp() }
                )
            }
            composable(
                route = InventoryItemDetailsDestination.routeWithArgs,
                arguments = listOf(navArgument(InventoryItemDetailsDestination.itemIdArg) {
                    type = NavType.IntType
                })
            ) {
                caseTheme = TYPE_THEME.COMPOSE_INVENTORY
                InventoryItemDetailsScreen(
                    navigateToEditItem = { navController.navigate("${InventoryItemEditDestination.route}/$it") },
                    navigateBack = { navController.navigateUp() }
                )
            }
            composable(
                route = InventoryItemEditDestination.routeWithArgs,
                arguments = listOf(navArgument(InventoryItemEditDestination.itemIdArg) {
                    type = NavType.IntType
                })
            ) {
                caseTheme = TYPE_THEME.COMPOSE_INVENTORY
                InventoryItemEditScreen(
                    navigateBack = { navController.popBackStack() },
                    onNavigateUp = { navController.navigateUp() }
                )
            }


//            Bus schedule
            composable(route = BusScheduleDestinations.FullSchedule.name) {
                val fullScheduleTitle = stringResource(R.string.bus_schedule_full_schedule)
                var topAppBarTitle by remember { mutableStateOf(fullScheduleTitle) }

                val onBackHandler = {
                    topAppBarTitle = fullScheduleTitle
                    navController.navigateUp()
                }

                Scaffold(
                    topBar = {
                        BusScheduleTopAppBar(
                            title = topAppBarTitle,
                            canNavigateBack = navController.previousBackStackEntry != null,
                            onBackClick = { onBackHandler() }
                        )
                    }
                ) { innerPadding ->

                    val viewModel: BusScheduleViewModel =
                        viewModel(factory = BusScheduleViewModel.factory)
                    val fullSchedule by viewModel.uiStateFull.collectAsState()

                    FullScheduleScreen(
                        modifier = Modifier.padding(innerPadding),
                        busSchedules = fullSchedule,
                        onScheduleClick = { busStopName ->
                            navController.navigate(
                                "${BusScheduleDestinations.RouteSchedule.name}/$busStopName"
                            )
                            topAppBarTitle = busStopName
                        }
                    )
                }
            }

            val busRouteArgument = "busRoute"
            composable(
                route = BusScheduleDestinations.RouteSchedule.name + "/{$busRouteArgument}",
                arguments = listOf(navArgument(busRouteArgument) { type = NavType.StringType })
            ) { backStackEntry ->

                val stopName = backStackEntry.arguments?.getString(busRouteArgument)
                    ?: error("busRouteArgument cannot be null")

                var topAppBarTitle by remember { mutableStateOf(stopName) }

                val onBackHandler = {
                    topAppBarTitle = stopName
                    navController.navigateUp()
                }
                Scaffold(
                    topBar = {
                        BusScheduleTopAppBar(
                            title = topAppBarTitle,
                            canNavigateBack = navController.previousBackStackEntry != null,
                            onBackClick = { onBackHandler() }
                        )
                    }
                ) { innerPadding ->

                    val viewModel: BusScheduleViewModel =
                        viewModel(factory = BusScheduleViewModel.factory)
                    viewModel.getScheduleFor(stopName)
                    val routeSchedule by viewModel.uiStateRoute.collectAsState()

                    RouteScheduleScreen(
                        modifier = Modifier.padding(innerPadding),
                        stopName = stopName,
                        busSchedules = routeSchedule,
                        onBack = { onBackHandler() }
                    )
                }
            }
        }
    }
}

//-----------------------------------
/*

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
                                val currentJetNewsRoute = navBackStackEntry?.destination?.route
                                    ?: MainJetNewsDestinations.HOME_ROUTE

                                AppDrawer(
                                    currentRoute = currentJetNewsRoute,
                                    navigateToHome = { navController.navigate(MainJetNewsDestinations.HOME_ROUTE) },
                                    navigateToInterests = { navController.navigate(MainJetNewsDestinations.INTERESTS_ROUTE) },
                                    closeDrawer = { coroutineScope.launch { scaffoldState.drawerState.close() } }
                                )
                            }

                            else -> {
                                Text("ABC")
                            }
                        }
                    }
                ) { innerPadding ->
                    HighroadNavHost(
                        appJetNewsContainer = appJetNewsContainer,
                        windowWidthClass = windowWidthClass,
                        devicePosture = devicePosture,
                        innerPadding = innerPadding,
                        navController = navController,
                        state = { config -> viewModel.configStates(config) },
                        scaffoldState = scaffoldState
                    )
                }
            }
        }
    }
}

@Composable
fun HighroadNavHost(
    appJetNewsContainer: AppJetNewsContainer,
    windowWidthClass: WindowWidthSizeClass,
    devicePosture: DevicePosture,
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


    }
}
*/

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

enum class TYPE_THEME {
    MAIN,
    COMPOSE_INTRODUCE,
    COMPOSE_BASICLAYOUT,
    COMPOSE_STATES,
    COMPOSE_THEMING,
    COMPOSE_ANIMATION,
    COMPOSE_CRANE,
    COMPOSE_RALLY,
    COMPOSE_JETNEWS,
    COMPOSE_INVENTORY,
    COMPOSE_BUSSCHEDULE,
    ARTICLES,
    TICTACTOE
}

@Composable
fun NavigationThemeSwitcher(
    typeAppTheme: TYPE_THEME,
    content: @Composable () -> Unit
) {
    when (typeAppTheme) {
        TYPE_THEME.MAIN -> EntranceTheme(content = content)
        TYPE_THEME.COMPOSE_INTRODUCE -> IntroduceTheme(content = content)
        TYPE_THEME.COMPOSE_BASICLAYOUT -> BasicLayoutsTheme(content = content)
        TYPE_THEME.COMPOSE_STATES -> StatesTheme(content = content)
        TYPE_THEME.COMPOSE_THEMING -> ReplyTheme(content = content)
        TYPE_THEME.COMPOSE_ANIMATION -> AnimationCodelabTheme(content = content)
        TYPE_THEME.COMPOSE_CRANE -> CraneTheme(content = content)
        TYPE_THEME.COMPOSE_RALLY -> RallyTheme(content = content)
        TYPE_THEME.COMPOSE_JETNEWS -> JetnewsTheme(content = content)
        TYPE_THEME.ARTICLES -> ArticlesTheme(content = content)
        TYPE_THEME.TICTACTOE -> TicTacToeTheme(content = content)
        TYPE_THEME.COMPOSE_INVENTORY -> InventoryTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                content()
            }
        }

        TYPE_THEME.COMPOSE_BUSSCHEDULE -> BusScheduleTheme(content = content)
    }
}