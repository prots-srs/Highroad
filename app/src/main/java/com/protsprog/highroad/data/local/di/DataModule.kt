package com.protsprog.highroad.data.local.di

import android.content.Context
import androidx.room.Room
import com.protsprog.highroad.data.local.database.ArticleDao
import com.protsprog.highroad.data.local.database.HighroadDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideArticleDao(appDatabase: HighroadDatabase): ArticleDao {
        return appDatabase.articleDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): HighroadDatabase {
        return Room.databaseBuilder(
            appContext,
            HighroadDatabase::class.java,
            "highroad"
        ).fallbackToDestructiveMigration().build()
    }
}