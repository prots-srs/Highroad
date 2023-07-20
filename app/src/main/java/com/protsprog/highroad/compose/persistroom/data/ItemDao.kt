package com.protsprog.highroad.compose.persistroom.data

/*
TO READ
https://developer.android.com/reference/androidx/room/OnConflictStrategy.html
https://www.sqlitetutorial.net/
https://medium.com/androiddevelopers/7-pro-tips-for-room-fbadea4bfbd1
 */
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("SELECT * from inventories WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    @Query("SELECT * from inventories ORDER BY name ASC")
    fun getAllItems(): Flow<List<Item>>
}