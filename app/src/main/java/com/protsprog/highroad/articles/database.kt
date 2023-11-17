package com.protsprog.highroad.articles

import android.content.Context
import androidx.room.ColumnInfo
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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    @ColumnInfo(defaultValue = "")
    val sort: String,
    @ColumnInfo(defaultValue = "")
    val description: String,
    @ColumnInfo(defaultValue = "")
    val picture: String
)

fun ArticleEntity.asModel() = ArticleListModel(
    aid = id,
    sort = sort.toInt(),
    title = title,
//    description = description,
    picture = picture
)

fun ArticleEntity.asItemModel() = ArticleItemModel(
    id = id,
    sort = sort.toInt(),
    title = title,
    description = description,
    picture = picture
)

@Dao
interface ArticleDao {
    //    @Query("SELECT * FROM articles ORDER BY sort + 0 ASC")
    @Query("SELECT * FROM articles ORDER BY id ASC")
    fun getAll(): Flow<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ArticleEntity>)

    @Query("DELETE FROM articles")
    suspend fun deleteAll()

//    @Delete
//    suspend fun delete(entity: ArticleEntity)

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertItem(item: ArticleEntity)

    @Query("SELECT * FROM articles WHERE id = :id")
    fun getItem(id: Int): Flow<ArticleEntity>
}

@Database(entities = [ArticleEntity::class], version = 9)
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