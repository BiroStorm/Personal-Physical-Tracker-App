package it.lam.pptproject.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import it.lam.pptproject.data.UserPreferencesDataStore
import it.lam.pptproject.data.room.AppDatabase
import it.lam.pptproject.data.room.User
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class LandingViewModel @Inject constructor(
    private val
    application: Application
) : AndroidViewModel(application) {

    private val context = application.applicationContext
    val username = mutableStateOf("")

    fun saveUserName() {
        viewModelScope.launch {
            val user = User(username = username.value, active = true, id = 0)
            AppDatabase.getDatabase(context).userDao().insertUser(user)
        }
    }

    fun checkActiveUser(onActiveUserFound: () -> Unit) {
        viewModelScope.launch {
            val activeUser = AppDatabase.getDatabase(context).userDao().findActive()
            if (activeUser != null) {
                onActiveUserFound()
            }
        }
    }
}