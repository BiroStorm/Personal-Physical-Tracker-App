package it.lam.pptproject.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.lam.pptproject.api.FitnessAPI
import it.lam.pptproject.data.ButtonStateDataStore
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

    init {
        viewModelScope.launch {
            ButtonStateDataStore.getApplicationState(context).collect { savedState ->
                _isTerminated.value = savedState
            }
        }
    }
    fun toggleState() {
        _isTerminated.value = !_isTerminated.value
        viewModelScope.launch {
            ButtonStateDataStore.saveApplicationState(context, _isTerminated.value)
            if (_isTerminated.value) {
                FitnessAPI.subscribeToFitnessData(context)
            } else {
                FitnessAPI.unsubscribeFromFitnessData(context)
            }
        }
    }

    fun readFitnessData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            FitnessAPI.readData(context)
        }else{
            Log.e("HomeViewModel", "readFitnessData: Unsupported Android version")
        }
    }


}