package com.protsprog.highroad.flightsearch.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.flightsearch.ui.AirportUIState
import com.protsprog.highroad.flightsearch.ui.theme.FlightSearchTheme

@Composable
fun SuggestionList(
    list: List<AirportUIState>,
    onSelectDeparture: (String) -> Unit,
    listState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        state = listState
    ) {
        items(list) {
            SuggestionItem(
                item = it,
                onSelectDeparture = onSelectDeparture
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 375)
@Composable
private fun SuggestionListPreview() {
    FlightSearchTheme {
        SuggestionList(
            list = demoPreviewList,
            onSelectDeparture = {}
        )
    }
}

@Composable
fun SuggestionItem(
    modifier: Modifier = Modifier,
    item: AirportUIState,
    onSelectDeparture: (String) -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelectDeparture(item.code) }
            .padding(top = 6.dp, bottom = 6.dp)
    ) {
        Text(
            modifier = modifier.width(48.dp),
            text = item.code,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            modifier = modifier.padding(start = 8.dp),
            text = item.name
        )
    }
}

@Preview(showBackground = true, widthDp = 375)
@Composable
private fun SuggestionItemPreview() {
    FlightSearchTheme {
        SuggestionItem(
            item = demoPreviewList[3]
        )
    }
}

private val demoPreviewList = listOf(
    AirportUIState(
//        id = 1,
        name = "Francisco Sá Carneiro Airport",
        code = "OPO"
    ),
    AirportUIState(
//        id = 2,
        name = "Stockholm Arlanda Airport",
        code = "ARN"
    ),
    AirportUIState(
//        id = 3,
        name = "Marseille Provence Airport",
        code = "MRS"
    ),
    AirportUIState(
//        id = 4,
        name = "Warsaw Chopin Airport",
        code = "WAW"
    )
)