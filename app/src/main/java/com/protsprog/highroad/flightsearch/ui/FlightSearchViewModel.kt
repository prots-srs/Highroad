package com.protsprog.highroad.flightsearch.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.protsprog.highroad.flightsearch.data.FlightSearchRepository
import com.protsprog.highroad.flightsearch.data.asUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

//private val DEBUG_TAG = "DEBUG_FLIGHT_VM"
@HiltViewModel
class FlightSearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repo: FlightSearchRepository
) : ViewModel() {
    var uiState by mutableStateOf(ListsUiState())

    val favoriteState: StateFlow<List<FavoriteState>> = repo.getAllFavoritesStream()
        .map { favoriteEntityList -> favoriteEntityList.map { favoriteEntity -> favoriteEntity.asUiState() } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = emptyList()
        )

    init {
        collectStoredSearchRequest()
    }

    private fun collectStoredSearchRequest() {
        viewModelScope.launch {
            repo.searchString.collect { searchString ->
                uiState = uiState.copy(
                    searchRequest = searchString,
                )
                getSuggestionList()
            }
        }
    }

    /*
    trigger show list
     */
    private suspend fun getSuggestionList() {
        if (uiState.searchRequest.isNotEmpty()) {
            prepareSuggestionList()
        } else {
            prepareFavoriteList()
        }
    }

    /*
    Suggestion list
     */
    private suspend fun prepareSuggestionList() {
        repo.getAirportBySearch(uiState.searchRequest).collect {
            uiState = uiState.copy(
                suggestionList = it.map { airportEntity -> airportEntity.asUiState() },
                showSuggestions = true,
                showFavorites = false,
                showRoutes = false
            )
        }
    }

    /*
    Favorites list
     */
    private suspend fun prepareFavoriteList() {
        val airportList = getAirportList()

        val routeList = favoriteState.value.map {
            RoutesUIState(
                departureCode = it.departureCode,
                departureName = airportList.getValue(it.departureCode),
                destinationCode = it.destinationCode,
                destinationName = airportList.getValue(it.destinationCode),
                isFavorite = true
            )
        }
        uiState = uiState.copy(
            suggestionList = emptyList(),
            routeList = routeList,
            showSuggestions = false,
            showRoutes = false,
            showFavorites = routeList.isNotEmpty()
        )
    }

    fun onClickSetSearch(input: String = "") {
        uiState = uiState.copy(
            searchRequest = input,
        )
        viewModelScope.launch {
            getSuggestionList()
        }

        viewModelScope.launch {
            repo.persistSearchString(input)
        }
    }

    private suspend fun getAirportList(): Map<String, String> = repo.getAllFlight().associate {
        it.iataCode to it.name
    }

    fun selectDeparture(iata: String) {
        viewModelScope.launch {
            val airportList = getAirportList()

            uiState = uiState.copy(departureChosen = iata,
                showSuggestions = false,
                showFavorites = false,
                showRoutes = true,
                suggestionList = emptyList(),
                routeList = airportList.filter { it.key != iata }.map { destination ->
                    val hasFavorite = favoriteState.value.filter {
                        it.departureCode == iata && it.destinationCode == destination.key
                    }

                    RoutesUIState(
                        departureCode = iata,
                        departureName = airportList.getValue(iata),
                        destinationCode = destination.key,
                        destinationName = destination.value,
                        isFavorite = hasFavorite.isNotEmpty()
                    )
                })
        }
    }

    fun setFavoriteFromRoutes(departCode: String, arriveCode: String) {
        var indexRoute: Int? = null
        var currentFavorite = false
        uiState = uiState.copy(routeList = uiState.routeList.mapIndexed { index, route ->
            if (route.departureCode == departCode && route.destinationCode == arriveCode) {
                indexRoute = index
                currentFavorite = route.isFavorite

                route.copy(
                    isFavorite = !route.isFavorite
                )
            } else {
                route
            }
        })

        indexRoute?.let {
            val route = uiState.routeList[indexRoute!!]
            viewModelScope.launch {
                if (currentFavorite) {
                    val hasFavorite = favoriteState.value.filter {
                        it.departureCode == departCode && it.destinationCode == arriveCode
                    }
                    if (hasFavorite.isNotEmpty()) {
                        repo.unFavorite(hasFavorite.first().toEntity())
                    }
                } else {
                    repo.isFavorite(route.toFavoriteEntity())
                }
            }
        }
    }

    fun unsetFavoriteFromFavorites(departCode: String, arriveCode: String) {
        viewModelScope.launch {
            val hasFavorite = favoriteState.value.filter {
                it.departureCode == departCode && it.destinationCode == arriveCode
            }
            if (hasFavorite.isNotEmpty()) {
                repo.unFavorite(hasFavorite.first().toEntity())
            }

            getSuggestionList()
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L

        /*val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HighroadApplication)
                FlightSearchViewModel(
                    repo = FlightSearchRepository(
                        dao = application.containerFlightSearch.database.flightDao(),
                        store = application.containerFlightSearch.store
                    )
                )
            }
        }*/
    }
}
