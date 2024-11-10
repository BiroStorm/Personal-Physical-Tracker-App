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
import it.lam.pptproject.service.ActivityDetectionService
import it.lam.pptproject.service.TrackingService
import it.lam.pptproject.utils.Utils
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

    private var isAutomatic = false

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
        this.isAutomatic = false
        viewModelScope.launch {
            Intent(appContext, TrackingService::class.java).also {
                it.action = TrackingService.Actions.START.toString()
                it.putExtra("selectedOption", selectedOption)
                appContext.startService(it)
            }
        }
    }

    private fun stopTrackingService() {
        try {
            if (isAutomatic) {

                Intent(appContext, ActivityDetectionService::class.java).also {
                    it.action = ActivityDetectionService.Actions.STOP.toString()
                    appContext.startService(it)
                }

            }else {
                Intent(appContext, TrackingService::class.java).also {
                    it.action = TrackingService.Actions.STOP.toString()
                    appContext.startService(it)
                }
            }
        } catch (e: Exception) {
            Log.e("HomeViewModel2", "stopTrackingService: ${e.message}")
        }

    }

    fun startDetectionService() {
        this.isAutomatic = true
        viewModelScope.launch {
            Intent(appContext, ActivityDetectionService::class.java).also {
                it.action = ActivityDetectionService.Actions.START.toString()
                appContext.startService(it)
            }
        }
    }


}
