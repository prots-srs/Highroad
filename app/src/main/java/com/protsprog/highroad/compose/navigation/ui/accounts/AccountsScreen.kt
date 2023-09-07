package com.protsprog.highroad.compose.navigation.ui.accounts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.R
import com.protsprog.highroad.compose.navigation.data.UserData
import com.protsprog.highroad.compose.navigation.ui.components.AccountRow
import com.protsprog.highroad.compose.navigation.ui.components.RallyTopAppBar
import com.protsprog.highroad.compose.navigation.ui.components.StatementBody
import com.protsprog.highroad.nav.RallyAccounts
import com.protsprog.highroad.nav.RallyOverview
import com.protsprog.highroad.nav.rallyTabRowScreens

/**
 * The Accounts screen.
 */
@Composable
fun AccountsScreen(
    onTabSelected: (String) -> Unit = {},
    onAccountClick: (String) -> Unit = {},
) {
    val amountsTotal = remember { UserData.accounts.map { account -> account.balance }.sum() }
    Scaffold(
        topBar = {
            RallyTopAppBar(
                allScreens = rallyTabRowScreens,
                onTabSelected = onTabSelected,
                currentScreenRoute = RallyAccounts.route
            )
        }
    ) { innerPadding ->
        StatementBody(
            modifier = Modifier
                .semantics { contentDescription = "Accounts Screen" }
                .padding(innerPadding)
                .padding(24.dp),
            items = UserData.accounts,
            amounts = { account -> account.balance },
            colors = { account -> account.color },
            amountsTotal = amountsTotal,
            circleLabel = stringResource(R.string.navigation_total),
            rows = { account ->
                AccountRow(
                    modifier = Modifier.clickable {
                        onAccountClick(account.name)
                    },
                    name = account.name,
                    number = account.number,
                    amount = account.balance,
                    color = account.color
                )
            }
        )
    }
}

/**
 * Detail screen for a single account.
 */
@Composable
fun SingleAccountScreen(
    onTabSelected: (String) -> Unit = {},
    accountType: String? = UserData.accounts.first().name
) {
    val account = remember(accountType) { UserData.getAccount(accountType) }
    Scaffold(
        topBar = {
            RallyTopAppBar(
                allScreens = rallyTabRowScreens,
                onTabSelected = onTabSelected,
                currentScreenRoute = RallyAccounts.route
            )
        }
    ) { innerPadding ->
        StatementBody(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp),
            items = listOf(account),
            colors = { account.color },
            amounts = { account.balance },
            amountsTotal = account.balance,
            circleLabel = account.name,
        ) { row ->
            AccountRow(
                name = row.name,
                number = row.number,
                amount = row.balance,
                color = row.color
            )
        }
    }
}
