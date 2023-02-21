package ua.com.biz_s.highroad.articles.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

//import androidx.room.*
//import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles ORDER BY sort ASC")
    fun getAll(): Flow<List<DatabaseArticle>>

//    @Query("SELECT * FROM articles WHERE aid = :id")
//    fun getItem(id: Int): Flow<DatabaseArticle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<DatabaseArticle>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: DatabaseArticle)
}

@Database(entities = [DatabaseArticle::class], version = 7, exportSchema = false)
abstract class HighroadDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    //... dao for another tables

    companion object {
        @Volatile
        private var INSTANCE: HighroadDatabase? = null

        fun getDatabase(context: Context): HighroadDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    HighroadDatabase::class.java,
                    "highroad"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}