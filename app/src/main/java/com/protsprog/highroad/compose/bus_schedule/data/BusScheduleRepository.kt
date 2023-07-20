package com.protsprog.highroad.compose.bus_schedule.data

import kotlinx.coroutines.flow.Flow

interface ItemsRepository {
    fun getAllItemsStream(): Flow<List<BusSchedule>>
    fun getItemByName(name: String): Flow<List<BusSchedule>>
}

class BusScheduleRepository(private val scheduleDao: BusScheduleDao) : ItemsRepository {
    override fun getAllItemsStream(): Flow<List<BusSchedule>> = scheduleDao.getAllItems()
    override fun getItemByName(name: String): Flow<List<BusSchedule>> = scheduleDao.getItemByName(name)
}
