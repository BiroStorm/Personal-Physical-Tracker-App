package it.lam.pptproject.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.lam.pptproject.data.UserPreferencesDataStore
import it.lam.pptproject.data.datastore.DataStoreRepository
import it.lam.pptproject.model.room.AppDatabase
import it.lam.pptproject.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val dsRepository: DataStoreRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    var activeUser = userRepository.getActiveUser().asLiveData()

    fun clearActiveUser() {
        viewModelScope.launch {
            userRepository.setAllUsersInactive()
            dsRepository.putString("username", "")
        }
    }
}