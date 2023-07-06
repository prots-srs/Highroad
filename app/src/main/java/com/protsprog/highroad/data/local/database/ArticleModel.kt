package com.protsprog.highroad.data.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.protsprog.highroad.data.model.ArticleAnonce
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

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

fun ArticleEntity.asExternalModel() = ArticleAnonce(
    title = title,
    description = description,
    picture = picture
)

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles ORDER BY sort + 0 ASC")
    fun getAll(): Flow<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ArticleEntity>)

    @Query("DELETE FROM articles")
    fun deleteAll()

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertItem(item: ArticleEntity)

//    @Query("SELECT * FROM articles WHERE aid = :id")
//    fun getItem(id: Int): Flow<DatabaseArticle>
}