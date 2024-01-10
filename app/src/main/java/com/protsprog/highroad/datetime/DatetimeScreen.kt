package com.protsprog.highroad.datetime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.protsprog.highroad.R
import com.protsprog.highroad.flightsearch.ui.components.FlightTopBar
import com.protsprog.highroad.motioncase.ui.components.GameTopBar


@Composable
fun DatetimeScreen(
    modifier: Modifier = Modifier,
    viewModel: DatetimeViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            FlightTopBar(
                title = stringResource(R.string.title_datetime_case), backClick = onNavigateUp
            )
        },
//        containerColor = MaterialTheme.colorScheme.onPrimary,
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_medium)),
            ) {
                    Text("Local datetime: ${viewModel.uIState.localDateTime}")
                    Text("Format datetime: ${viewModel.uIState.datetimeFormatted}")
                    Text("Local date: ${viewModel.uIState.localDate}")
                    Text("Local time: ${viewModel.uIState.localTime}")
                    Text("time after one hour: ${viewModel.uIState.dateTimeAfterOneHour}")
                    Text("time after one day: ${viewModel.uIState.dateTimeAfterOneDay}")
                    Text("time 30 minutes earlier: ${viewModel.uIState.dateTime30MinEarlier}")
                    Text("other day: ${viewModel.uIState.dayOfYear}")
                    Text("days in other day: ${viewModel.uIState.daysInMonth}")
                    Text("next month: ${viewModel.uIState.nextMonth}")
                    Text("prev month: ${viewModel.uIState.prevMonth}")
                    Text("range days: ${viewModel.uIState.range}")
            }
        }
    }
}
