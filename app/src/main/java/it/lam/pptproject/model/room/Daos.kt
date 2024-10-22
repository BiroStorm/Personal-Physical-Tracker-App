package it.lam.pptproject.model.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import it.lam.pptproject.data.room.TypePercentageData

/**
 * Il DAO è un'interfaccia che permette di interragire con il database.
 * Le operazioni CRUD vengono fatti sul DAO.
 *
 */

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE active IS 1 LIMIT 1")
    suspend fun findActive(): User?

    @Insert(entity = User::class)
    suspend fun insertUser(user: User)

    @Update
    suspend fun changeUser(ser: User)

    @Delete
    suspend fun delete(user: User)
}

@Dao
interface TrackingDataDao {
    @Insert
    suspend fun insert(trackingData: TrackingData)

    @Query("SELECT * FROM TrackingData WHERE username = :username")
    suspend fun getActivities(username: String): List<TrackingData>
}

@Dao
interface StatisticsDao {

    // * Calcola la percentuale di ogni tipo di attività.
    @Query("SELECT type, (SUM(type) / (SELECT SUM(type) FROM TrackingData)) * 100 AS percentage FROM TrackingData WHERE username = :username GROUP BY type")
    suspend fun getPercentuale(username: String): List<TypePercentageData>
}