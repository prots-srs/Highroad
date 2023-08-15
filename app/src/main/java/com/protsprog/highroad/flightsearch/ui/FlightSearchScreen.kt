package com.protsprog.highroad.flightsearch.ui

/*
TO READ
https://developer.android.com/jetpack/compose/tooling/previews
https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary
https://developer.android.com/jetpack/compose/designsystems/material3
https://developer.android.com/jetpack/compose/text/style-paragraph
 */
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.protsprog.highroad.R
import com.protsprog.highroad.flightsearch.ui.components.FlightTopBar
import com.protsprog.highroad.flightsearch.ui.components.RoutesList
import com.protsprog.highroad.flightsearch.ui.components.SearchBar
import com.protsprog.highroad.flightsearch.ui.components.SuggestionList
import com.protsprog.highroad.flightsearch.ui.theme.FlightSearchTheme
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun FlightSearchScreen(
    onBackPressed: () -> Unit,
    viewModel: FlightSearchViewModel = hiltViewModel()
//    viewModel: FlightSearchViewModel = viewModel(factory = FlightSearchViewModel.Factory)
) {
    val uiState = viewModel.uiState
    viewModel.favoriteState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            FlightTopBar(
                title = stringResource(R.string.flight_search_app_name),
                backClick = onBackPressed
            )
        },
        containerColor = MaterialTheme.colorScheme.onPrimary,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(all = 16.dp)
        ) {
            SearchBar(
                inputText = uiState.searchRequest,
                onValueChange = viewModel::onClickSetSearch,
                onValueClear = viewModel::onClickSetSearch
            )

            AnimatedVisibility(visible = uiState.showSuggestions) {
                Row(modifier = Modifier.padding(top = 16.dp)) {
                    SuggestionList(
                        list = uiState.suggestionList,
                        onSelectDeparture = viewModel::selectDeparture
                    )
                }
            }

            AnimatedVisibility(visible = uiState.showRoutes) {
                Column(modifier = Modifier.padding(top = 24.dp)) {
                    Text(
                        text = "Flight from ${uiState.departureChosen}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.padding(top = 16.dp))
                    RoutesList(
                        list = uiState.routeList,
                        onFavorite = viewModel::setFavoriteFromRoutes
                    )
                }
            }

            AnimatedVisibility(visible = uiState.showFavorites) {
                Column(modifier = Modifier.padding(top = 24.dp)) {
                    Text(
                        text = "Favorite routes",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.padding(top = 16.dp))
                    RoutesList(
                        list = uiState.routeList,
                        onFavorite = viewModel::unsetFavoriteFromFavorites
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true)
@Composable
fun FlightSearchScreenPreview() {
    FlightSearchTheme {
        FlightSearchScreen(
            onBackPressed = {}
        )
    }
}