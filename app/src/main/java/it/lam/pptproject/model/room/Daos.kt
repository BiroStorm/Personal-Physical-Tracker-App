package it.lam.pptproject.model.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import it.lam.pptproject.data.room.TypePercentageData
import kotlinx.coroutines.flow.Flow

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
    @Query("""
        SELECT
    a.type,
    SUM(a.endTime - a.startTime) AS value,
    (SUM(a.endTime - a.startTime) * 100.0) / 
    (SELECT SUM(a2.endTime - a2.startTime)
     FROM  trackingdata a2
     JOIN User u2 ON a2.username = u2.username
     WHERE u2.active = 1) AS percentage
FROM trackingdata a
JOIN User u ON a.username = u.username
WHERE u.active = 1
GROUP BY a.type

    """)
    fun getPercentuale(): Flow<List<TypePercentageData>>
    // * From: [https://developer.android.com/codelabs/advanced-kotlin-coroutines?hl=en#9]
}