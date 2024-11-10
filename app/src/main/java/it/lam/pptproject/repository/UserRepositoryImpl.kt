package it.lam.pptproject.repository

import it.lam.pptproject.model.room.User
import it.lam.pptproject.model.room.UserDao
import kotlinx.coroutines.flow.Flow


interface UserRepository {
    fun getAllUser(): Flow<List<User>>
    suspend fun insertUser(user: User)
    suspend fun deleteUser(user: User)
    suspend fun updateUser(user: User)
    fun getActiveUser(): Flow<User?>
    suspend fun setAllUsersInactive()
}

class UserRepositoryImpl (private val userDao: UserDao) : UserRepository{

    override fun getAllUser(): Flow<List<User>> {
        return userDao.getAll()
    }

    override suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    override suspend fun deleteUser(user: User) {
        userDao.delete(user)
    }

    override suspend fun updateUser(user: User) {
        userDao.changeUser(user)
    }


    override fun getActiveUser(): Flow<User?> {
        return userDao.findActiveTest()
    }

    override suspend fun setAllUsersInactive() {
        userDao.setAllUsersInactive()
    }
}
