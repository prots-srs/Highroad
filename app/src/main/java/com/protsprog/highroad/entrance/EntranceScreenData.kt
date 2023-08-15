package com.protsprog.highroad.entrance

import com.protsprog.highroad.R
import com.protsprog.highroad.nav.Articles
import com.protsprog.highroad.nav.Compose
import com.protsprog.highroad.nav.FlightSearch
import com.protsprog.highroad.nav.MotionCase
import com.protsprog.highroad.nav.TicTacToe

data class EntranceItem(
    val picture: Int,
    val title: String,
    val destination: String
)

val entranceItems = listOf<EntranceItem>(
    EntranceItem(
        picture = R.drawable.banner_motion,
        title = "Motion case",
        destination = MotionCase.route
    ),
    EntranceItem(
        picture = R.drawable.banner_flight_search,
        title = "Flight Search case",
        destination = FlightSearch.route
    ),
    EntranceItem(
        picture = R.drawable.banner_tictaktoe,
        title = "TicTacToe case",
        destination = TicTacToe.route
    ),
    EntranceItem(
        picture = R.drawable.banner_compose,
        title = "Compose case",
        destination = Compose.route
    ),
    EntranceItem(
        picture = R.drawable.banner_article,
        title = "Articles case",
        destination = Articles.route
    ),
)