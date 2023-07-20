package com.protsprog.highroad.compose.bus_schedule.data

/*
TO READ
https://developer.android.com/training/data-storage/room/prepopulate
 */
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

private val nameDatabase = "busschedule"

@Database(entities = [BusSchedule::class], version = 2, exportSchema = false)
abstract class BusScheduleDatabase : RoomDatabase() {
    abstract fun itemDao(): BusScheduleDao

    companion object {
        @Volatile
        private var Instance: BusScheduleDatabase? = null

        fun getDatabase(context: Context): BusScheduleDatabase = Instance ?: synchronized(this) {
            Room.databaseBuilder(context, BusScheduleDatabase::class.java, nameDatabase)
                .createFromAsset("database/bus_schedule.db")
                .fallbackToDestructiveMigration()
                .build()
                .also { Instance = it }
        }
    }
}