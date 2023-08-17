package com.protsprog.highroad

/*
TO READ
https://developer.android.com/training/dependency-injection/hilt-android
https://developer.android.com/jetpack/compose/libraries#hilt
https://medium.com/mobile-app-development-publication/dagger-hilt-on-multi-module-android-app-26815c427fb
 */
import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.protsprog.highroad.compose.accessibility.data.AppJetNewsContainer
import com.protsprog.highroad.compose.accessibility.data.AppJetNewsContainerImpl
import com.protsprog.highroad.compose.bus_schedule.data.AppBusScheduleContainer
import com.protsprog.highroad.compose.bus_schedule.data.AppDataBusScheduleContainer
import com.protsprog.highroad.compose.datastore.data.UserPreferencesRepository
import com.protsprog.highroad.compose.persistroom.data.AppDataInventoryContainer
import com.protsprog.highroad.compose.persistroom.data.AppInventoryContainer
import com.protsprog.highroad.util.UnsplashSizingInterceptor
import dagger.hilt.android.HiltAndroidApp

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "layout_preferences")

@HiltAndroidApp
class HighroadApplication : Application(), ImageLoaderFactory {

    lateinit var containerForJetNews: AppJetNewsContainer
    lateinit var containerInventory: AppInventoryContainer
    lateinit var containerBusSchedule: AppBusScheduleContainer

    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        containerForJetNews = AppJetNewsContainerImpl(this)
        containerInventory = AppDataInventoryContainer(this)
        containerBusSchedule = AppDataBusScheduleContainer(this)

        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                add(UnsplashSizingInterceptor)
            }
            .build()
    }
}
