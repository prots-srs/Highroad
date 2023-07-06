package com.protsprog.highroad

/*
TO READ
https://developer.android.com/codelabs/jetpack-compose-testing
https://developer.android.com/jetpack/compose/testing-cheatsheet
https://developer.android.com/jetpack/compose/semantics
https://developer.android.com/reference/kotlin/androidx/compose/ui/test/package-summary
https://developer.android.com/reference/kotlin/androidx/compose/ui/platform/package-summary#(androidx.compose.ui.Modifier).testTag(kotlin.String)
 */

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.text.toUpperCase
import com.protsprog.highroad.compose.navigation.ui.components.RallyTopAppBar
import com.protsprog.highroad.compose.navigation.ui.theme.RallyTheme
import com.protsprog.highroad.nav.RallyAccounts
import com.protsprog.highroad.nav.rallyTabRowScreens
import org.junit.Rule
import org.junit.Test

class TopAppBarTest {
    //    by testing a component in isolation
    @get:Rule
    val rule = createComposeRule()
//    rule{.finder}{.assertion}{.action}

    @Test
    fun rallyTopAppBarTest() {
        rule.setContent {
            RallyTheme {
                RallyTopAppBar(
                    allScreens = rallyTabRowScreens,
                    onTabSelected = { },
                    currentScreenRoute = RallyAccounts.route
                )
            }
        }
        rule.onRoot(useUnmergedTree = true).printToLog("currentLabelExists")

        rule
            .onNode(
                hasText(RallyAccounts.title.uppercase()) and
                        hasParent(
                            hasContentDescription(RallyAccounts.title)
                        ),
                useUnmergedTree = true
            )
//            .onNodeWithContentDescription(RallyAccounts.title)
            .assertExists()
//            .onNodeWithContentDescription(RallyAccounts.title.uppercase())
//            .assertIsSelected()
    }
}