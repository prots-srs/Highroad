package com.protsprog.highroad.todo

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "todolist")
data class ToDoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val text: String,
    val sort: Int,
    val closed: Boolean
)

fun ToDoEntity.asUIState() = WorkTask(
    id = id,
    text = text,
    sort = sort,
    closed = closed
)

@Dao
interface ToDoListDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE, entity = ToDoEntity::class)
    suspend fun insert(entity: ToDoEntity)

    @Update(entity = ToDoEntity::class)
    suspend fun update(entity: ToDoEntity)

    @Delete(entity = ToDoEntity::class)
    suspend fun delete(entity: ToDoEntity)

    @Query("SELECT * from todolist ORDER BY sort ASC")
    fun getAll(): Flow<List<ToDoEntity>>

    @Query("SELECT * from todolist WHERE sort = :sort")
    fun getTask(sort: Int): ToDoEntity
}

@Database(entities = [ToDoEntity::class], version = 1)
abstract class ToDoListDatabase : RoomDatabase() {
    abstract fun todoDao(): ToDoListDao
}