package com.protsprog.highroad.compose.bus_schedule.data

import android.content.Context

interface AppBusScheduleContainer {
    val itemsRepository: ItemsRepository
}

class AppDataBusScheduleContainer(private val context: Context) : AppBusScheduleContainer {
    override val itemsRepository: ItemsRepository by lazy {
        BusScheduleRepository(BusScheduleDatabase.getDatabase(context).itemDao())
    }
}