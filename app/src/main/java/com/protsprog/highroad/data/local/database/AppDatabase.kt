package com.protsprog.highroad.data.local.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ArticleEntity::class], version = 9)
abstract class HighroadDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}
/*
@Database(entities = [ArticleEntity::class], version = 7, exportSchema = false)
abstract class HighroadDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    //... dao for another tables

    companion object {
        @Volatile
        private var INSTANCE: HighroadDatabase? = null

        fun getDatabase(context: Context): HighroadDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    HighroadDatabase::class.java,
                    "highroad"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}

 */