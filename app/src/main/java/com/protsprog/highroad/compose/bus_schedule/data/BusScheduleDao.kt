package com.protsprog.highroad.compose.bus_schedule.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BusScheduleDao {
    @Query("SELECT * from Schedule ORDER BY arrival_time ASC")
    fun getAllItems(): Flow<List<BusSchedule>>

    @Query("SELECT * from Schedule WHERE stop_name = :name")
    fun getItemByName(name: String): Flow<List<BusSchedule>>
}