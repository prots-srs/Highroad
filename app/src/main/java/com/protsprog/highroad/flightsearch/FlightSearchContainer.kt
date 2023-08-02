package com.protsprog.highroad.flightsearch

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.protsprog.highroad.flightsearch.data.FlightSearchDatabase

interface AppFlightSearchContainer {
    val database: FlightSearchDatabase
    val store: DataStore<Preferences>
}

class AppDataFlightSearchContainer(private val context: Context) : AppFlightSearchContainer {
    override val database: FlightSearchDatabase by lazy {
        FlightSearchDatabase.getDatabase(context)
    }

    override val store: DataStore<Preferences> by lazy {
        context.dataStore
    }
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "layout_flightsearch")