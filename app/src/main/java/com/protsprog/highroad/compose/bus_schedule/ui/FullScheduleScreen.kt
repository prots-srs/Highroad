package com.protsprog.highroad.compose.bus_schedule.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.protsprog.highroad.compose.bus_schedule.data.BusSchedule
import com.protsprog.highroad.compose.bus_schedule.ui.components.BusScheduleScreen
import com.protsprog.highroad.compose.bus_schedule.ui.theme.BusScheduleTheme

@Composable
fun FullScheduleScreen(
    busSchedules: List<BusSchedule>,
    onScheduleClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    BusScheduleScreen(
        busSchedules = busSchedules,
        onScheduleClick = onScheduleClick,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun FullScheduleScreenPreview() {
    BusScheduleTheme {
        FullScheduleScreen(
            busSchedules = List(3) { index ->
                BusSchedule(
                    index,
                    "Main Street",
                    111111
                )
            },
            onScheduleClick = {}
        )
    }
}