package it.lam.pptproject.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

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