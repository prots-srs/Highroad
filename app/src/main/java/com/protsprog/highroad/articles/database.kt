package com.protsprog.highroad.articles

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Dao
interface ArticleDao {
    //    @Query("SELECT * FROM articles ORDER BY sort + 0 ASC")
    @Query("SELECT * FROM articles ORDER BY id ASC")
    fun getAll(): Flow<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ArticleEntity>)

    @Query("DELETE FROM articles")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(item: ArticleEntity)

    @Insert//(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ArticleEntity)

    @Update
    suspend fun updateItem(item: ArticleEntity)

    suspend fun upsertItem(item: ArticleEntity) {
        try {
            insertItem(item)
//            Log.d("TEST_FLOW", "database insert: ${item}")
        } catch (e: SQLiteConstraintException) {
            updateItem(item)
//            Log.d("TEST_FLOW", "database update: ${item}")
        }
    }

    @Query("SELECT * FROM articles WHERE id = :id")
    fun getItemStream(id: Int): Flow<ArticleEntity>

    @Query("SELECT * FROM articles WHERE id = :id")
    suspend fun getItem(id: Int): ArticleEntity
}

@Database(entities = [ArticleEntity::class], version = 10)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideArticleDao(appDatabase: ArticleDatabase): ArticleDao {
        return appDatabase.articleDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): ArticleDatabase {
        return Room.databaseBuilder(
            appContext,
            ArticleDatabase::class.java,
            "highroad"
        ).fallbackToDestructiveMigration().build()
    }
}
