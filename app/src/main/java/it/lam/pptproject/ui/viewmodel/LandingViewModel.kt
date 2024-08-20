package it.lam.pptproject.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import it.lam.pptproject.data.UserPreferencesDataStore
import it.lam.pptproject.model.room.AppDatabase
import it.lam.pptproject.model.room.User
import it.lam.pptproject.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class LandingViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val
    application: Application
) : AndroidViewModel(application) {



    private val context = application.applicationContext
    val username = mutableStateOf("")
    val users = mutableStateOf<List<User>>(emptyList())


    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            users.value = withContext(Dispatchers.IO) {
                userRepository.getAllUser()
            }
        }
    }

    fun setActiveUser(user: User, onActiveUserSet: () -> Unit) {
        viewModelScope.launch {
            userRepository.updateUser(user.copy(active = true))
            UserPreferencesDataStore.saveUsername(context, user.username)
            onActiveUserSet()
        }
    }

    fun saveUsername() {
        viewModelScope.launch {
            val user = User(username = username.value, active = true)
            AppDatabase.getDatabase(context).userDao().insertUser(user)

            // Save username to DataStore
            UserPreferencesDataStore.saveUsername(context, username.value)

        }
    }

    fun deleteUser(user: User) {
    viewModelScope.launch {
        withContext(Dispatchers.IO) {
            userRepository.deleteUser(user)
            // todo: Eliminare tutti i dati associati all'utente.
            // riaggiorna la lista degli utenti
            fetchUsers()
        }
    }
}

    fun checkActiveUser(onActiveUserFound: () -> Unit) {
        viewModelScope.launch {
            val activeUser = AppDatabase.getDatabase(context).userDao().findActive()
            val username = UserPreferencesDataStore.getUsername(context).firstOrNull()

            if (activeUser != null && username != null) {
                onActiveUserFound()
            }
        }
    }
}