package com.protsprog.highroad.compose.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf

class WellnessTask(
    val id: Int,
    val label: String,
    initialChecked: Boolean = false
) {
    var checked by mutableStateOf(initialChecked)
}

fun getWellnessTasks() = List(30) { i -> WellnessTask(i, "Task # $i") }