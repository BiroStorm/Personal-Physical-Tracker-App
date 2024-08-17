package it.lam.pptproject.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.lam.pptproject.repository.UserRepository
import it.lam.pptproject.ui.viewmodel.LandingViewModel

class LandingViewModelFactory(
    private val userRepository: UserRepository,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LandingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LandingViewModel(userRepository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}