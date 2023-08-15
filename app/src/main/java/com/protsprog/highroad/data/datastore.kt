package com.protsprog.highroad.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TictactoeDataStore
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FlightSearchDataStore

@InstallIn(SingletonComponent::class)
@Module
object DataStoreModule {
    @Singleton
    @TictactoeDataStore
    @Provides
    fun provideTicTacToePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile("layout_tictaktoe")
            }
        )
    @Singleton
    @FlightSearchDataStore
    @Provides
    fun provideFlightSearchPreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile("layout_flightsearch")
            }
        )
}