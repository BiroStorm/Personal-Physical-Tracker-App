package it.lam.pptproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import it.lam.pptproject.data.datastore.DataStoreRepository
import it.lam.pptproject.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class BaseScreenViewModel @Inject constructor(
    private val dsRepository: DataStoreRepository,
    private val userRepository: UserRepository
) : ViewModel(){

    val activeUser = userRepository.getActiveUser().asLiveData()

/*
    suspend fun thereIsAnActiveUser() : Boolean {
        val result = dsRepository.getString("activeUser")
        return result != null

    }
 */
}