package it.lam.pptproject.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.lam.pptproject.api.FitnessAPI
import it.lam.pptproject.data.ButtonStateDataStore
import it.lam.pptproject.data.UserPreferencesDataStore
import it.lam.pptproject.utils.Tracker
import it.lam.pptproject.utils.saveTrackingData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application
)  : AndroidViewModel(application){

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun toggleState() {
        _isTerminated.value = !_isTerminated.value
        viewModelScope.launch {
            ButtonStateDataStore.saveApplicationState(context, _isTerminated.value)
            if (_isTerminated.value) {

                Tracker.start()
                val username = UserPreferencesDataStore.getUsername(context).first() ?: "unknown"
                Tracker.setUsername(username)
                // ONLY IF THE TYPE IS WALKING ACTIVE FITNESS API
                if (_currentType.value == Tracker.RecordType.WALKING) {
                    FitnessAPI.subscribeToFitnessData(context)
                }
            } else {
                Tracker.stop()
                if (_currentType.value == Tracker.RecordType.WALKING) {
                    FitnessAPI.saveData(context)
                    FitnessAPI.unsubscribeFromFitnessData(context)
                }else {
                    saveTrackingData(context)
                    Tracker.clearData()
                }
            }
        }
    }

    fun readFitnessData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            FitnessAPI.saveData(context)
        }else{
            Log.e("HomeViewModel", "readFitnessData: Unsupported Android version")
        }
    }

    fun updateTrackerType(type: Tracker.RecordType) {
        _currentType.value = type
        Tracker.setType(type)
    }


}