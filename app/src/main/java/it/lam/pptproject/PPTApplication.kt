package it.lam.pptproject

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import dagger.hilt.android.HiltAndroidApp
import it.lam.pptproject.api.FitnessAPI
import it.lam.pptproject.repository.TrackingRepository
import it.lam.pptproject.utils.Tracker
import it.lam.pptproject.utils.saveTrackingData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// DO NOT TOUCH ! used by Hilt.
@HiltAndroidApp
class PPTApplication : Application(){

    private lateinit var trackingRepository: TrackingRepository

    // TODO: non va....
    override fun onTerminate() {
        Log.i("PPTApplication", "Terminating app...")
        CoroutineScope(Dispatchers.IO).launch {

            Log.i("PPTApplication", "Trying to end the tracking.")
            // todo: bisognerebbe controllare se è ancora in listening...
            trackingRepository.endTracking()

            super.onTerminate()
        }
    }
}