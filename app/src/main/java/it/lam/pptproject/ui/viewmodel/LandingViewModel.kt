package it.lam.pptproject.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import it.lam.pptproject.data.UserPreferencesDataStore
import it.lam.pptproject.data.room.AppDatabase
import it.lam.pptproject.data.room.User
import it.lam.pptproject.repository.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class LandingViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val
    application: Application
) : AndroidViewModel(application) {

    private val context = application.applicationContext
    val username = mutableStateOf("")

    fun saveUsername() {
        viewModelScope.launch {
            val user = User(username = username.value, active = true, id = 0)
            AppDatabase.getDatabase(context).userDao().insertUser(user)

            // Save username to DataStore
            UserPreferencesDataStore.saveUsername(context, username.value)

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