package com.protsprog.highroad.flightsearch.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.flightsearch.ui.RoutesUIState
import com.protsprog.highroad.flightsearch.ui.theme.FlightSearchTheme
import com.protsprog.highroad.flightsearch.ui.theme.md_theme_light_isFavorite
import com.protsprog.highroad.flightsearch.ui.theme.md_theme_light_isNoFavorite

@Composable
fun RoutesList(
    list: List<RoutesUIState>,
    onFavorite: (String, String) -> Unit,
    listState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = listState
    ) {
        items(list) { routeState ->
            RouteItem(
                item = routeState,
                onFavorite = onFavorite,
            )
        }
    }
}

@Composable
fun RouteItem(
    modifier: Modifier = Modifier,
    item: RoutesUIState,
    onFavorite: (String, String) -> Unit = { _: String, _: String -> },
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.tertiary,
        contentColor = MaterialTheme.colorScheme.onTertiary,
        shape = RoundedCornerShape(
            topStart = CornerSize(0),
            topEnd = CornerSize(20.dp),
            bottomStart = CornerSize(0),
            bottomEnd = CornerSize(0),
        )
    ) {
        Row(
            modifier = modifier
                .padding(all = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                RouteItemLine(
                    title = "depart",
                    code = item.departureCode,
                    name = item.departureName
                )
                Spacer(modifier = modifier.padding(top = 8.dp))
                RouteItemLine(
                    title = "arrive",
                    code = item.destinationCode,
                    name = item.destinationName
                )
            }
            Column(
                modifier = modifier.width(50.dp),
                horizontalAlignment = Alignment.End
            ) {
                /*Image(
                    modifier = modifier
                        .clickable { onFavorite(departCode, arriveCode) }
                        .background(Color.Magenta)
                        .size(36.dp),
                    painter = painterResource(id = R.drawable.ic_star_rate_48),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(tintFavorite)
                )*/
                Icon(
                    modifier = modifier
                        .clickable { onFavorite(item.departureCode, item.destinationCode) }
//                        .background(Color.Yellow)
                        .size(36.dp),
                    imageVector = Icons.Filled.StarRate,
                    tint = if (item.isFavorite) md_theme_light_isFavorite else md_theme_light_isNoFavorite,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
fun RouteItemLine(
    modifier: Modifier = Modifier, title: String, code: String, name: String
) {
    Text(
        text = title.uppercase(), style = MaterialTheme.typography.labelMedium
    )
    Row(
        modifier = modifier.padding(top = 4.dp, bottom = 4.dp)
    ) {
        Text(
            modifier = modifier.width(48.dp), text = code, fontWeight = FontWeight.ExtraBold
        )
        Text(
            modifier = modifier.padding(start = 8.dp), text = name
        )
    }
}

@Preview(showBackground = true, widthDp = 375, backgroundColor = 0xFF00FF00)
@Composable
private fun RoutesListPreview() {
    FlightSearchTheme {
        RouteItem(
            item = RoutesUIState(
                departureCode = "OPO",
                departureName = "Francisco Sá Carneiro Airport",
                destinationCode = "ARN",
                destinationName = "Stockholm Arlanda Airport Stockholm Arlanda Airport",
                isFavorite = true
            )
        )
    }
}