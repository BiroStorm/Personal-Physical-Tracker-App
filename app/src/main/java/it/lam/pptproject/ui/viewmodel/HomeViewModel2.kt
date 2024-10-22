package it.lam.pptproject.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.lam.pptproject.data.ButtonStateDataStore
import it.lam.pptproject.repository.TrackingRepository
import kotlinx.coroutines.flow.first

class HomeViewModel2(private val repository: TrackingRepository) : ViewModel() {
    var hasStarted by mutableStateOf(false)
        private set

    init {
        Log.i("HomeViewModel2", "init: ")
    }

    fun switchState() {

        hasStarted = !hasStarted
    }
}
