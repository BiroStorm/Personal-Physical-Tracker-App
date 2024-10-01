package it.lam.pptproject.ui.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import it.lam.pptproject.data.ButtonStateDataStore
import it.lam.pptproject.data.UserPreferencesDataStore
import it.lam.pptproject.repository.TrackingRepository
import it.lam.pptproject.utils.Tracker
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class HomeViewModel(
    private val trackingRepository: TrackingRepository,
    application: Application
) : AndroidViewModel(application) {


    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext
    private val _isTerminated = mutableStateOf(false)
    val isTerminated: State<Boolean> get() = _isTerminated
    private val _currentType = mutableStateOf(Tracker.RecordType.WALKING)
    val currentType: State<Tracker.RecordType> get() = _currentType

    init {
        viewModelScope.launch {
            ButtonStateDataStore.getApplicationState(context).collect { savedState ->
                _isTerminated.value = savedState
            }
            _currentType.value = Tracker.getType()
        }
    }

    fun toggleState() {
        if (!checkPermissions()) {
            // * Non si hanno ancora i permessi, ignorare il click.
            return
        }
        _isTerminated.value = !_isTerminated.value
        viewModelScope.launch {
            ButtonStateDataStore.saveApplicationState(context, _isTerminated.value)
            if (_isTerminated.value) {
                Tracker.start()
                val username = UserPreferencesDataStore.getUsername(context).first() ?: "unknown"
                Tracker.setUsername(username)

                trackingRepository.startTracking()
            } else {
                Tracker.stop()
                trackingRepository.endTracking()
            }
        }
    }

    fun readFitnessData() {
        viewModelScope.launch {
            trackingRepository.printDati()
        }
    }

    fun updateTrackerType(type: Tracker.RecordType) {
        _currentType.value = type
        Tracker.setType(type)
    }



    private fun checkPermissions(): Boolean {
        return context.checkSelfPermission(
            Manifest.permission.ACTIVITY_RECOGNITION
        ) == PackageManager.PERMISSION_GRANTED
    }



}