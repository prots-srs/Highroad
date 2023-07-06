package com.protsprog.highroad

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.protsprog.highroad.nav.HighroadNavHost
import com.protsprog.highroad.nav.NavigationViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EntranceTest {
    @get:Rule
    val rule = createComposeRule()
//    val rule = createAndroidComposeRule<ComponentActivity>()

//    val stringText = rule.activity.getString(R.string.navigation_see_all)

    lateinit var navController: TestNavHostController

    @Before
    fun setupHighroadNavHost() {
        rule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(
                ComposeNavigator()
            )

            val viewModel: NavigationViewModel = viewModel()
            HighroadNavHost(
                windowWidthClass = WindowWidthSizeClass.Compact,
                navController = navController,
                state = { config -> viewModel.configStates(config) },
                innerPadding = PaddingValues()
            )
        }
    }

    @Test
    fun check_Found_Title() {
        val card = hasText("Articles case") and hasClickAction()
//        rule.onNodeWithText("Articles case").perf
        rule.onNode(card)
        rule
//            .onAllNodes(hasText("Articles"))
            .onNodeWithContentDescription("Articles case")
            .assertIsDisplayed()
//            .assertExists()
    }
}