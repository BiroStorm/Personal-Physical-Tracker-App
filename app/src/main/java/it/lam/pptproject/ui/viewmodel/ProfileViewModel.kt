package it.lam.pptproject.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.lam.pptproject.data.room.AppDatabase
import it.lam.pptproject.data.room.User
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext
    val activeUser = mutableStateOf<User?>(null)

    fun fetchActiveUser() {
        viewModelScope.launch {
            activeUser.value = AppDatabase.getDatabase(context).userDao().findActive()
        }
    }
}