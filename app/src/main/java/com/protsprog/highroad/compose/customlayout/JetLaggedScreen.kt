package com.protsprog.highroad.compose.customlayout

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.compose.customlayout.ui.theme.SmallHeadingStyle
import com.protsprog.highroad.compose.customlayout.ui.theme.Yellow
import com.protsprog.highroad.compose.customlayout.ui.theme.YellowVariant
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@Preview(showBackground = true)
@Preview(device = Devices.FOLDABLE, showBackground = true)
@Composable
fun JetLaggedScreen(
    modifier: Modifier = Modifier,
    onDrawerClicked: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .background(Color.White)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.yellowBackground()) {
            JetLaggedHeader(
                onDrawerClicked = onDrawerClicked,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            JetLaggedSleepSummary(modifier = Modifier.padding(start = 16.dp, end = 16.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        var selectedTab by remember { mutableStateOf(SleepTab.Week) }
        JetLaggedHeaderTabs(
            onTabSelected = { selectedTab = it },
            selectedTab = selectedTab,
        )

        Spacer(modifier = Modifier.height(16.dp))
        val sleepState by remember {
            mutableStateOf(sleepData)
        }
        JetLaggedTimeGraph(sleepState)
    }
}

@Composable
private fun JetLaggedTimeGraph(sleepGraphData: SleepGraphData) {
    val scrollState = rememberScrollState()

    val hours = (sleepGraphData.earliestStartHour..23) + (0..sleepGraphData.latestEndHour)

    TimeGraph(
        modifier = Modifier
            .horizontalScroll(scrollState)
            .wrapContentSize(),
        dayItemsCount = sleepGraphData.sleepDayData.size,
        hoursHeader = {
            HoursHeader(hours)
        },
        dayLabel = { index ->
            val data = sleepGraphData.sleepDayData[index]
            DayLabel(data.startDate.dayOfWeek)
        },
        bar = { index ->
            val data = sleepGraphData.sleepDayData[index]
            // We have access to Modifier.timeGraphBar() as we are now in TimeGraphScope
            SleepBar(
                sleepData = data,
                modifier = Modifier.padding(bottom = 8.dp)
                    .timeGraphBar(
                        start = data.firstSleepStart,
                        end = data.lastSleepEnd,
                        hours = hours,
                    )
            )
        }
    )
}

@Composable
private fun DayLabel(dayOfWeek: DayOfWeek) {
    Text(
        dayOfWeek.getDisplayName(
            TextStyle.SHORT, Locale.getDefault()
        ),
        Modifier
            .height(24.dp)
            .padding(start = 8.dp, end = 24.dp),
        style = SmallHeadingStyle,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun HoursHeader(hours: List<Int>) {
    Row(
        Modifier
            .padding(bottom = 16.dp)
            .drawBehind {
                val brush = Brush.linearGradient(listOf(YellowVariant, Yellow))
                drawRoundRect(
                    brush,
                    cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx()),
                )
            }
    ) {
        hours.forEach {
            Text(
                text = "$it",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(50.dp)
                    .padding(vertical = 4.dp),
                style = SmallHeadingStyle
            )
        }
    }
}