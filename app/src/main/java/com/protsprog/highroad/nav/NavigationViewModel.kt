package com.protsprog.highroad.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.protsprog.highroad.articles.ui.theme.ArticlesTheme
import com.protsprog.highroad.compose.accessibility.ui.theme.JetnewsTheme
import com.protsprog.highroad.compose.animating.ui.theme.AnimationCodelabTheme
import com.protsprog.highroad.compose.basiclayouts.theme.BasicLayoutsTheme
import com.protsprog.highroad.compose.navigation.ui.theme.RallyTheme
import com.protsprog.highroad.compose.sideeffects.ui.CraneTheme
import com.protsprog.highroad.compose.states.theme.StatesTheme
import com.protsprog.highroad.compose.theming.ui.theme.ReplyTheme
import com.protsprog.highroad.entrance.ui.theme.EntranceTheme
import com.protsprog.highroad.tictactoe.ui.theme.TicTacToeTheme
import com.protsprog.highroad.ui.theme.IntroduceTheme

class NavigationViewModel : ViewModel() {

    var uiState by mutableStateOf(NavigationUiState())
        private set

    fun configStates(config: CONFIGS_STATES) {
        when (config) {
            CONFIGS_STATES.ENTRANCE -> {
                uiState = uiState.copy(
                    currentScreenTitle = Entrance.title,
                    currentScreenRoute = Entrance.route,
                    typeAppTheme = TYPE_THEME.MAIN,
                    typeAppTopBar = TYPE_TOPBAR.NONE
                )
            }
            CONFIGS_STATES.COMPOSE -> {
                uiState = uiState.copy(
                    currentScreenTitle = Compose.title,
                    currentScreenRoute = Compose.route,
                    typeAppTheme = TYPE_THEME.MAIN,
                    typeAppTopBar = TYPE_TOPBAR.MAIN
                )
            }
            CONFIGS_STATES.COMPOSE_CASE -> {
                uiState = uiState.copy(
                    currentScreenTitle = ComposeCase.title,
                    currentScreenRoute = ComposeCase.route,
                    typeAppTheme = TYPE_THEME.MAIN,
                    typeAppTopBar = TYPE_TOPBAR.NONE
                )
            }
            CONFIGS_STATES.COMPOSE_CASE_1 -> {
                uiState = uiState.copy(
                    currentScreenTitle = ComposeCase.title,
                    currentScreenRoute = ComposeCase.route,
                    typeAppTheme = TYPE_THEME.COMPOSE_INTRODUCE,
                    typeAppTopBar = TYPE_TOPBAR.NONE
                )
            }
            CONFIGS_STATES.COMPOSE_CASE_2 -> {
                uiState = uiState.copy(
                    currentScreenTitle = ComposeCase.title,
                    currentScreenRoute = ComposeCase.route,
                    typeAppTheme = TYPE_THEME.COMPOSE_BASICLAYOUT,
                    typeAppTopBar = TYPE_TOPBAR.NONE
                )
            }
            CONFIGS_STATES.COMPOSE_CASE_3 -> {
                uiState = uiState.copy(
                    currentScreenTitle = ComposeCase.title,
                    currentScreenRoute = ComposeCase.route,
                    typeAppTheme = TYPE_THEME.COMPOSE_STATES,
                    typeAppTopBar = TYPE_TOPBAR.NONE
                )
            }
            CONFIGS_STATES.COMPOSE_CASE_4 -> {
                uiState = uiState.copy(
                    currentScreenTitle = ComposeCase.title,
                    currentScreenRoute = ComposeCase.route,
                    typeAppTheme = TYPE_THEME.COMPOSE_THEMING,
                    typeAppTopBar = TYPE_TOPBAR.NONE
                )
            }
            CONFIGS_STATES.COMPOSE_CASE_5 -> {
                uiState = uiState.copy(
                    currentScreenTitle = ComposeCase.title,
                    currentScreenRoute = ComposeCase.route,
                    typeAppTheme = TYPE_THEME.COMPOSE_ANIMATION,
                    typeAppTopBar = TYPE_TOPBAR.NONE
                )
            }
            CONFIGS_STATES.COMPOSE_CASE_6 -> {
                uiState = uiState.copy(
                    currentScreenTitle = ComposeCase.title,
                    currentScreenRoute = ComposeCase.route,
                    typeAppTheme = TYPE_THEME.COMPOSE_CRANE,
                    typeAppTopBar = TYPE_TOPBAR.NONE
                )
            }
            CONFIGS_STATES.COMPOSE_CASE_MAP -> {
                uiState = uiState.copy(
                    currentScreenTitle = ComposeCaseMap.title,
                    currentScreenRoute = ComposeCaseMap.route,
                    typeAppTheme = TYPE_THEME.COMPOSE_CRANE,
                    typeAppTopBar = TYPE_TOPBAR.NONE
                )
            }

            CONFIGS_STATES.COMPOSE_CASE_JETNEWS_HOME -> {
                uiState = uiState.copy(
                    currentScreenTitle = "",
                    currentScreenRoute = MainJetNewsDestinations.HOME_ROUTE,
                    typeAppTheme = TYPE_THEME.COMPOSE_JETNEWS,
                    typeAppTopBar = TYPE_TOPBAR.JETNEWS
                )
            }
            CONFIGS_STATES.COMPOSE_CASE_JETNEWS_ARTICLE -> {
                uiState = uiState.copy(
                    currentScreenTitle = "",
                    currentScreenRoute = MainJetNewsDestinations.ARTICLE_ROUTE,
                    typeAppTheme = TYPE_THEME.COMPOSE_JETNEWS,
                    typeAppTopBar = TYPE_TOPBAR.JETNEWS
                )
            }
            CONFIGS_STATES.COMPOSE_CASE_JETNEWS_INTERESTS -> {
                uiState = uiState.copy(
                    currentScreenTitle = "",
                    currentScreenRoute = MainJetNewsDestinations.INTERESTS_ROUTE,
                    typeAppTheme = TYPE_THEME.COMPOSE_JETNEWS,
                    typeAppTopBar = TYPE_TOPBAR.JETNEWS
                )
            }

            CONFIGS_STATES.ARTICLES -> {
                uiState = uiState.copy(
                    currentScreenTitle = Articles.title,
                    currentScreenRoute = Articles.route,
                    typeAppTheme = TYPE_THEME.ARTICLES,
                    typeAppTopBar = TYPE_TOPBAR.MAIN
                )
            }
            CONFIGS_STATES.TICTACTOE -> {
                uiState = uiState.copy(
                    currentScreenTitle = TicTacToe.title,
                    currentScreenRoute = TicTacToe.route,
                    typeAppTheme = TYPE_THEME.TICTACTOE,
                    typeAppTopBar = TYPE_TOPBAR.MAIN
                )
            }
            CONFIGS_STATES.COMPOSE_RALLY_OVERVIEW -> {
                uiState = uiState.copy(
                    currentScreenTitle = RallyOverview.title,
                    currentScreenRoute = RallyOverview.route,
                    typeAppTheme = TYPE_THEME.COMPOSE_RALLY,
                    typeAppTopBar = TYPE_TOPBAR.RALLY
                )
            }

            CONFIGS_STATES.COMPOSE_RALLY_ACCOUNTS -> {
                uiState = uiState.copy(
                    currentScreenTitle = RallyAccounts.title,
                    currentScreenRoute = RallyAccounts.route,
                    typeAppTheme = TYPE_THEME.COMPOSE_RALLY,
                    typeAppTopBar = TYPE_TOPBAR.RALLY
                )
            }
            CONFIGS_STATES.COMPOSE_RALLY_BILLS -> {
                uiState = uiState.copy(
                    currentScreenTitle = RallyBills.title,
                    currentScreenRoute = RallyBills.route,
                    typeAppTheme = TYPE_THEME.COMPOSE_RALLY,
                    typeAppTopBar = TYPE_TOPBAR.RALLY
                )
            }
            CONFIGS_STATES.COMPOSE_RALLY_SINGLE_ACCOUNT -> {
                uiState = uiState.copy(
                    currentScreenTitle = RallySingleAccount.title,
                    currentScreenRoute = RallySingleAccount.route,
                    typeAppTheme = TYPE_THEME.COMPOSE_RALLY,
                    typeAppTopBar = TYPE_TOPBAR.RALLY
                )
            }

            else -> {

            }
        }
    }
}

data class NavigationUiState(
    var currentScreenRoute: String = Entrance.route,
    var currentScreenTitle: String = Entrance.title,
    var typeAppTheme: TYPE_THEME = TYPE_THEME.MAIN,
    var typeAppTopBar: TYPE_TOPBAR = TYPE_TOPBAR.NONE
)

enum class TYPE_THEME {
    NONE,
    MAIN,
    COMPOSE_INTRODUCE,
    COMPOSE_BASICLAYOUT,
    COMPOSE_STATES,
    COMPOSE_THEMING,
    COMPOSE_ANIMATION,
    COMPOSE_CRANE,
    COMPOSE_RALLY,
    COMPOSE_JETNEWS,
    ARTICLES,
    TICTACTOE
}

enum class TYPE_TOPBAR { NONE, MAIN, RALLY, JETNEWS}

enum class CONFIGS_STATES {
    ENTRANCE,
    COMPOSE,
    COMPOSE_CASE,
    COMPOSE_CASE_1,
    COMPOSE_CASE_2,
    COMPOSE_CASE_3,
    COMPOSE_CASE_4,
    COMPOSE_CASE_5,
    COMPOSE_CASE_6,
    COMPOSE_CASE_MAP,
    COMPOSE_CASE_JETNEWS_HOME,
    COMPOSE_CASE_JETNEWS_ARTICLE,
    COMPOSE_CASE_JETNEWS_INTERESTS,
    COMPOSE_RALLY_OVERVIEW,
    COMPOSE_RALLY_ACCOUNTS,
    COMPOSE_RALLY_BILLS,
    COMPOSE_RALLY_SINGLE_ACCOUNT,
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
        else -> content()
    }
}

