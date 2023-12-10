package com.protsprog.highroad.entrance.data

import com.protsprog.highroad.R
import com.protsprog.highroad.nav.Articles
import com.protsprog.highroad.nav.BluetoothCase
import com.protsprog.highroad.nav.CameraXCase
import com.protsprog.highroad.nav.Compose
import com.protsprog.highroad.nav.FlightSearch
import com.protsprog.highroad.nav.MotionCase
import com.protsprog.highroad.nav.TicTacToe
import com.protsprog.highroad.nav.ToDoCase

inline class StringResource(val value: Int)
data class EntranceItem(
    val id: Int,
    val picture: Int,
    val title: StringResource,
    val destination: String,
    val freeShow: Boolean = true
)

val entranceItems = listOf(
    EntranceItem(
        id = 1,
        picture = R.drawable.banner_article,
        title = StringResource(R.string.entrance_title_case_article),
        destination = Articles.route,
        freeShow = true
    ),
    EntranceItem(
        id = 8,
        picture = R.drawable.banner_camera,
        title = StringResource(R.string.entrance_camerax),
        destination = CameraXCase.route,
        freeShow = true
    ),
    EntranceItem(
        id = 7,
        picture = R.drawable.bluetothcase,
        title = StringResource(BluetoothCase.titleRes),
        destination = BluetoothCase.route,
        freeShow = true
    ),
    EntranceItem(
        id = 6,
        picture = R.drawable.todo_preview,
        title = StringResource(R.string.entrance_todo_case_title),
        destination = ToDoCase.route,
        freeShow = true
    ),
    EntranceItem(
        id = 5,
        picture = R.drawable.banner_motion,
        title = StringResource(R.string.entrance_title_case_motion),
        destination = MotionCase.route,
        freeShow = false
    ),
    EntranceItem(
        id = 4,
        picture = R.drawable.banner_flight_search,
        title = StringResource(R.string.entrance_title_case_flight_search),
        destination = FlightSearch.route
    ),
    EntranceItem(
        id = 3,
        picture = R.drawable.banner_tictaktoe,
        title = StringResource(R.string.entrance_title_case_tictactoe),
        destination = TicTacToe.route,
        freeShow = false
    ),
    EntranceItem(
        id = 2,
        picture = R.drawable.banner_compose,
        title = StringResource(R.string.entrance_title_case_compose),
        destination = Compose.route
    ),
)