package com.protsprog.highroad

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.protsprog.highroad.compose.navigation.ui.overview.OverviewScreen
import com.protsprog.highroad.compose.navigation.ui.theme.RallyTheme
import org.junit.Rule
import org.junit.Test

class OverviewScreenTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun overviewScreen_alertsDisplayed() {
        rule.setContent {
            RallyTheme {
                OverviewScreen()
            }
        }

        rule.onNodeWithText("Alerts")
            .assertIsDisplayed()

//        Thread.sleep(5000)
    }
}