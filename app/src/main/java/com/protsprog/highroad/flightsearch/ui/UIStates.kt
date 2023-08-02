package com.protsprog.highroad.flightsearch.ui

import com.protsprog.highroad.flightsearch.data.FavoriteEntity

data class AirportUIState(
    val name: String,
    val code: String
)
data class FavoriteState(
    val id: Int = 0,
    val departureCode: String,
    val destinationCode: String
)

fun FavoriteState.toEntity() = FavoriteEntity(
    id = id,
    departureCode = departureCode,
    destinationCode = destinationCode,
)
data class ListsUiState(
    val showSuggestions: Boolean = false,
    val showFavorites:Boolean = false,
    val showRoutes:Boolean = false,
    val searchRequest:String = "",
    val departureChosen: String = "",
    val suggestionList: List<AirportUIState> = emptyList(),
    val routeList: List<RoutesUIState> = emptyList()
)

data class RoutesUIState (
    val departureCode: String,
    val departureName: String,
    val destinationCode: String,
    val destinationName: String,
    val isFavorite: Boolean = false
)

fun RoutesUIState.toFavoriteEntity() = FavoriteEntity(
    departureCode = departureCode,
    destinationCode = destinationCode,
)