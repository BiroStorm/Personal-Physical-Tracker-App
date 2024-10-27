// VM Provider dato dal Tutorial ufficiale di Android Kotlin

package it.lam.pptproject.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import it.lam.pptproject.PPTApplication
import it.lam.pptproject.ui.viewmodel.ChartsViewModel
import it.lam.pptproject.ui.viewmodel.HomeViewModel2

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ChartsViewModel
        initializer {
            HomeViewModel2(inventoryApplication().container.trackingRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [PPTApplication].
 */
fun CreationExtras.inventoryApplication(): PPTApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as PPTApplication)
