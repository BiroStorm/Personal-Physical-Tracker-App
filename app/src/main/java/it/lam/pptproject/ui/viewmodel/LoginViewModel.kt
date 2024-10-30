package it.lam.pptproject.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.lam.pptproject.data.datastore.DataStoreRepository
import it.lam.pptproject.model.room.User
import it.lam.pptproject.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private var userRepository: UserRepository,
    private val dsRepository: DataStoreRepository,
) : ViewModel() {

    val users: LiveData<List<User>> = userRepository.getAllUser().asLiveData()

    private var isUserSelected = mutableStateOf(false)
    val hasUserBeenChosen: MutableState<Boolean> = isUserSelected

    fun setActiveUser(user: User) {
        Log.i("LandingViewModel", "setActiveUser: $user")
        viewModelScope.launch {
            user.active = true
            userRepository.updateUser(user)
            isUserSelected.value = true
            dsRepository.putString("username", user.username)

        }
    }

    fun resetUserSelection() {
        isUserSelected.value = false
    }

    fun insertNewUser(user: User) {
        viewModelScope.launch {
            userRepository.insertUser(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            userRepository.deleteUser(user)
        }
    }

}