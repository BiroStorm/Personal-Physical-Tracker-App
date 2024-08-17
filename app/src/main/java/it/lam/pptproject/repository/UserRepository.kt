package it.lam.pptproject.repository

import androidx.lifecycle.MutableLiveData
import it.lam.pptproject.data.room.User
import it.lam.pptproject.data.room.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class UserRepository (private val userDao: UserDao) {


    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun getAllUser(): List<User> {
        return userDao.getAll()
    }

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun deleteUser(user: User) {
        userDao.delete(user)
    }

    suspend fun updateUser(user: User) {
        userDao.changeUser(user)
    }

    suspend fun getActiveUser(): User? {
        return userDao.findActive()
    }
}
