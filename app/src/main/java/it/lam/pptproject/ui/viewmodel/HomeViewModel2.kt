package it.lam.pptproject.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.lam.pptproject.data.datastore.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel2 @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {
    var hasStarted by mutableStateOf(false)
        private set


    var isStarted: Flow<Boolean?> = dataStoreRepository.isTracking()

    init {
        Log.i("HomeViewModel2", "init: ")
    }

    fun switchState() {
        hasStarted = !hasStarted
        viewModelScope.launch {
            if (hasStarted) dataStoreRepository.setTracking(true)
            else
                dataStoreRepository.setTracking(false)
            Log.i("HomeViewModel2", "switchState: ${isStarted.first()}")
        }
    }

    fun startTracking() {
        hasStarted = true
        viewModelScope.launch {
            dataStoreRepository.setTracking(true)
        }
    }




}
