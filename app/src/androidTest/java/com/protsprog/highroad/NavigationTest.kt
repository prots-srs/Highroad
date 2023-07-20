package com.protsprog.highroad

/*
READ
https://developer.android.com/jetpack/compose/testing
 */
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.protsprog.highroad.nav.Compose
//import com.protsprog.highroad.nav.NavigationViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    lateinit var navController: TestNavHostController

    @Before
    fun setupHighroadNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(
                ComposeNavigator()
            )

//            val viewModel: NavigationViewModel = viewModel()
//            HighroadNavHost(
//                navController = navController,
//                state = { config -> viewModel.configStates(config) },
//                innerPadding = PaddingValues()
//            )
        }
    }

    @Test
    fun highroadNavHost_verifyOverviewStartDestination() {
        composeTestRule
//            .onAllNodes(hasText("Articles"))
            .onNodeWithContentDescription("Articles case")
            .assertIsDisplayed()
    }

    @Test
    fun highroadNavHost_click_navigatesToComposeAnimation() {
        composeTestRule
            .onNodeWithContentDescription("Compose case")
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("Compose animating elements")
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithText("Weather")
            .assertIsDisplayed()
    }

    @Test
    fun highroadNavHost_clickAllBills_navigateToBills() {
        composeTestRule
            .onNodeWithContentDescription("Compose case")
            .performScrollTo()
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, Compose.route)
    }

}