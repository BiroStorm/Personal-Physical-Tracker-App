package it.lam.pptproject.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.lam.pptproject.data.UserPreferencesDataStore
import it.lam.pptproject.model.room.AppDatabase
import it.lam.pptproject.model.room.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext
    val activeUser = mutableStateOf<String?>("")

    fun fetchActiveUser() {
        viewModelScope.launch {
            activeUser.value = UserPreferencesDataStore.getUsername(context).first()
        }
    }


    fun clearActiveUser() {
        viewModelScope.launch {
            // Clear username from DataStore
            UserPreferencesDataStore.clearUsername(context)

            // Set user to inactive in the database
            val userDao = AppDatabase.getDatabase(context).userDao()
            val activeUser = userDao.findActive()
            activeUser?.let {
                it.active = false
                userDao.changeUser(it)
            }
        }
    }
}