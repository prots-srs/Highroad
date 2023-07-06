package com.protsprog.highroad.compose.navigation.ui.bills

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import com.protsprog.highroad.R
import com.protsprog.highroad.compose.navigation.data.Bill
import com.protsprog.highroad.compose.navigation.data.UserData
import com.protsprog.highroad.compose.navigation.ui.components.BillRow
import com.protsprog.highroad.compose.navigation.ui.components.StatementBody
import com.protsprog.highroad.nav.HighroadDestination

/**
 * The Bills screen.
 */
@Composable
fun BillsScreen(
    bills: List<Bill> = remember { UserData.bills }
) {
    StatementBody(
        modifier = Modifier.clearAndSetSemantics { contentDescription = "Bills" },
        items = bills,
        amounts = { bill -> bill.amount },
        colors = { bill -> bill.color },
        amountsTotal = bills.map { bill -> bill.amount }.sum(),
        circleLabel = stringResource(R.string.navigation_due),
        rows = { bill ->
            BillRow(bill.name, bill.due, bill.amount, bill.color)
        }
    )
}
