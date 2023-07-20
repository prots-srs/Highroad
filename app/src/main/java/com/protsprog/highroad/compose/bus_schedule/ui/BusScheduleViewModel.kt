package com.protsprog.highroad.compose.bus_schedule.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.protsprog.highroad.HighroadApplication
import com.protsprog.highroad.compose.bus_schedule.data.BusSchedule
import com.protsprog.highroad.compose.bus_schedule.data.ItemsRepository
import com.protsprog.highroad.compose.persistroom.data.Item
import com.protsprog.highroad.compose.persistroom.ui.home.HomeUiState
import com.protsprog.highroad.compose.persistroom.ui.home.HomeViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class BusScheduleViewModel(private val scheduleRepository: ItemsRepository) : ViewModel() {

    val uiStateFull: StateFlow<List<BusSchedule>> = scheduleRepository.getAllItemsStream()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = listOf<BusSchedule>()
        )

    lateinit var uiStateRoute: StateFlow<List<BusSchedule>>

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L

        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                BusScheduleViewModel(busscheduleApplication().containerBusSchedule.itemsRepository)
            }
        }
    }

    fun getScheduleFor(stopName: String) {
        uiStateRoute = scheduleRepository.getItemByName(stopName)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = listOf<BusSchedule>()
            )
    }
}

fun CreationExtras.busscheduleApplication(): HighroadApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HighroadApplication)