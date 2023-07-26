package com.protsprog.highroad.tictactoe

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

data class GamePreferences(
    val nameCross: String = "",
    val nameNought: String = "",
    val scoreCross: Int = 0,
    val scoreNought: Int = 0
)

class TicTacToeRepository(
    private val dataStore: DataStore<Preferences>
) {
    val gamePreferences: Flow<GamePreferences> = dataStore.data
        .catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            GamePreferences(
                nameCross = preferences[PreferencesKeys.NAME_CROSS] ?: "Cross",
                nameNought = preferences[PreferencesKeys.NAME_NOUGHT] ?: "Nought",
                scoreCross = preferences[PreferencesKeys.SCORE_CROSS] ?: 0,
                scoreNought = preferences[PreferencesKeys.SCORE_NOUGHT] ?: 0,
            )
        }

    suspend fun saveNameCross(name: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NAME_CROSS] = name
        }
    }

    suspend fun saveNameNought(name: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NAME_NOUGHT] = name
        }
    }

    suspend fun saveScoreCross(score: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SCORE_CROSS] = score
        }
    }

    suspend fun saveScoreNought(score: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SCORE_NOUGHT] = score
        }
    }

    suspend fun clearData() {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NAME_CROSS] = "Cross"
            preferences[PreferencesKeys.NAME_NOUGHT] = "Nought"
            preferences[PreferencesKeys.SCORE_CROSS] = 0
            preferences[PreferencesKeys.SCORE_NOUGHT] = 0
        }
    }

    private object PreferencesKeys {
        val NAME_CROSS = stringPreferencesKey("name_cross")
        val NAME_NOUGHT = stringPreferencesKey("name_nought")
        val SCORE_CROSS = intPreferencesKey("score_cross")
        val SCORE_NOUGHT = intPreferencesKey("score_nought")
    }
}