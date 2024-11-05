package it.lam.pptproject.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import it.lam.pptproject.data.datastore.DataStoreRepository
import it.lam.pptproject.service.TrackingService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    @ApplicationContext private val appContext: Context,
) : ViewModel() {
    private var hasStarted by mutableStateOf(false)
        private set


    var isStarted: Flow<Boolean?> = dataStoreRepository.isTracking()

    init {
        Log.i("HomeViewModel2", "init: ")
    }

    fun switchState() {
        hasStarted = !hasStarted
        viewModelScope.launch {
            if (hasStarted) {
                dataStoreRepository.setTracking(true)
            } else {
                dataStoreRepository.setTracking(false)
                stopTrackingService()
            }

            Log.i("HomeViewModel2", "switchState: ${isStarted.first()}")
        }
    }

    // * Richiamato dalla UI per evitare un Bug.
    fun startTrackingService(selectedOption: String) {
        viewModelScope.launch {
            Intent(appContext, TrackingService::class.java).also {
                it.action = TrackingService.Actions.START.toString()
                it.putExtra("selectedOption", selectedOption)
                appContext.startService(it)
            }
        }
    }

    private fun stopTrackingService() {
        viewModelScope.launch {
            Intent(appContext, TrackingService::class.java).also {
                it.action = TrackingService.Actions.STOP.toString()
                appContext.startService(it)
            }
        }
    }


}
