package com.protsprog.highroad.todo.di

import android.content.Context
import androidx.room.Room
import com.protsprog.highroad.todo.ToDoListDao
import com.protsprog.highroad.todo.ToDoListDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
//@InstallIn(ViewModelComponent::class)
@InstallIn(SingletonComponent::class)
object ToDoDbModule {
    @Provides
    fun provideDao(db: ToDoListDatabase): ToDoListDao = db.todoDao()

//    @ViewModelScoped
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ToDoListDatabase =
        Room.databaseBuilder(context, ToDoListDatabase::class.java, "todolist_db")
            .fallbackToDestructiveMigration()
            .build()
}