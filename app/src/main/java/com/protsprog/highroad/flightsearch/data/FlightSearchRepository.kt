package com.protsprog.highroad.flightsearch.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.protsprog.highroad.data.FlightSearchDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class FlightSearchRepository @Inject constructor(
    private val dao: FlightSearchDao,
    @FlightSearchDataStore private val store: DataStore<Preferences>
) {
    fun getAirportBySearch(search: String): Flow<List<AirportEntity>> =
        dao.getAirportBySearch(search)

    suspend fun getAllFlight(): List<AirportEntity> = dao.getAirports()
    fun getAllFavoritesStream(): Flow<List<FavoriteEntity>> = dao.getFavorits()

    suspend fun isFavorite(item: FavoriteEntity) = dao.insert(item)
    suspend fun unFavorite(item: FavoriteEntity) = dao.delete(item)

    val searchString: Flow<String> = store.data
        .catch {
            if (it is IOException) {
//                Log.d("PERSIST_SEARCH error", it.toString())
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map {
            it[PreferencesKeys.SEARCH_STRING] ?: ""
        }

    suspend fun persistSearchString(searchString: String) {
        store.edit {
            it[PreferencesKeys.SEARCH_STRING] = searchString
        }
    }

    private object PreferencesKeys {
        val SEARCH_STRING = stringPreferencesKey("search_string")
    }
}