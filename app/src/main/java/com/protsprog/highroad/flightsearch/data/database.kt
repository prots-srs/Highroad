package com.protsprog.highroad.flightsearch.data

/*
TO READ
https://www.sqlite.org/lang_select.html

https://developer.android.com/codelabs/basic-android-kotlin-compose-sql

https://medium.com/androiddevelopers/database-relations-with-room-544ab95e4542

 */
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
import com.protsprog.highroad.flightsearch.ui.AirportUIState
import com.protsprog.highroad.flightsearch.ui.FavoriteState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "airport")
data class AirportEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    @ColumnInfo(name = "iata_code")
    val iataCode: String,
    val passengers: Int
)

fun AirportEntity.asUiState() = AirportUIState(
    code = iataCode,
    name = name
)

@Entity(tableName = "favorite")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "departure_code")
    val departureCode: String,
    @ColumnInfo(name = "destination_code")
    val destinationCode: String
)

fun FavoriteEntity.asUiState() = FavoriteState(
    id = id,
    departureCode = departureCode,
    destinationCode = destinationCode
)

@Dao
interface FlightSearchDao {
    @Query("SELECT * FROM airport WHERE name LIKE '%'||:search||'%' OR iata_code = UPPER(:search) ORDER BY passengers DESC")
    fun getAirportBySearch(search: String): Flow<List<AirportEntity>>

    @Query("SELECT * from airport ORDER BY passengers DESC")
    suspend fun getAirports(): List<AirportEntity>

    @Query("SELECT * from favorite ORDER BY id DESC")
    fun getFavorits(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE, entity = FavoriteEntity::class)
    suspend fun insert(entity: FavoriteEntity)

    @Delete(entity = FavoriteEntity::class)
    suspend fun delete(entity: FavoriteEntity)
}

private const val databaseNameFlight = "flights"

@Database(entities = [AirportEntity::class, FavoriteEntity::class], version = 2)
abstract class FlightSearchDatabase : RoomDatabase() {
    abstract fun flightDao(): FlightSearchDao

    /*companion object {
        @Volatile
        private var Instance: FlightSearchDatabase? = null

        fun getDatabase(context: Context): FlightSearchDatabase = Instance ?: synchronized(this) {
            Room.databaseBuilder(context, FlightSearchDatabase::class.java, databaseNameFlight)
//                .createFromAsset("database/flight_search.db")
                .fallbackToDestructiveMigration()
                .build()
                .also { Instance = it }
        }
    }*/
}

@Module
@InstallIn(ViewModelComponent::class)
object DatabaseModule {
    @Provides
    fun provideDao(database: FlightSearchDatabase): FlightSearchDao = database.flightDao()

    @ViewModelScoped
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): FlightSearchDatabase =
        Room.databaseBuilder(context, FlightSearchDatabase::class.java, databaseNameFlight)
            .createFromAsset("database/flight_search.db")
            .fallbackToDestructiveMigration()
            .build()
}