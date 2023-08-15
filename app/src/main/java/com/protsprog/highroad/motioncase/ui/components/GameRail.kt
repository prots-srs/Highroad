package com.protsprog.highroad.motioncase.ui.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.R
import com.protsprog.highroad.flightsearch.ui.theme.FlightSearchTheme

@Composable
fun GameRail(
    onBackPressed: () -> Unit = {}
) {
    NavigationRail(
        modifier = Modifier.fillMaxHeight(),
        containerColor = MaterialTheme.colorScheme.tertiary,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        NavigationRailItem(
            selected = false,
            onClick = onBackPressed,
            icon = {
                Icon(
                    modifier = Modifier
                        .width(32.dp)
                        .height(32.dp),
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(
                        R.string.bus_schedule_back
                    ),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GameRailPreview() {
    FlightSearchTheme(useDarkTheme = false) {
        GameRail()
    }
}