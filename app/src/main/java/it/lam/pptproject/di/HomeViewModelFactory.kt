package it.lam.pptproject.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.lam.pptproject.repository.TrackingRepository
import it.lam.pptproject.ui.viewmodel.HomeViewModel

class HomeViewModelFactory(
    private val trackingRepository: TrackingRepository,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(trackingRepository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}