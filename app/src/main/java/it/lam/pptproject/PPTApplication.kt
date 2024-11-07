package it.lam.pptproject

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.fitness.FitnessLocal
import com.google.android.gms.fitness.LocalRecordingClient
import com.google.android.gms.fitness.data.LocalDataType
import dagger.hilt.android.HiltAndroidApp
import it.lam.pptproject.data.datastore.DataStoreRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

// DO NOT TOUCH ! used by Hilt.
@HiltAndroidApp
class PPTApplication : Application() {

    private lateinit var localRecordingClient: LocalRecordingClient


    @Inject
    lateinit var dataStoreRepository: DataStoreRepository

    override fun onCreate() {
        super.onCreate()

        // Initialize the DataStore
        CoroutineScope(Dispatchers.IO).launch {
            dataStoreRepository.initializeDefaults()
        }

        // Initialize the Notification Channel
        setupNotificationChannel()

        // Perform a Play Services version check
        val hasMinPlayServices = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
            this,
            LocalRecordingClient.LOCAL_RECORDING_CLIENT_MIN_VERSION_CODE
        )
        if (hasMinPlayServices != ConnectionResult.SUCCESS) {
            Log.w("MainActivity", "Google Play Services troppo vecchia!")
        }

        localRecordingClient = FitnessLocal.getLocalRecordingClient(this)

    }

    @SuppressLint("MissingPermission")
    fun subscribeToStepCount() {
        localRecordingClient.subscribe(LocalDataType.TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener {
                Log.i("PPTApplication", "Successfully subscribed!")
            }
            .addOnFailureListener { e ->
                Log.w("PPTApplication", "There was a problem subscribing.", e)
            }
    }


    override fun onTerminate() {
        localRecordingClient.unsubscribe(LocalDataType.TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener {
                Log.i("PPTApplication", "Successfully unsubscribed!")
            }
            .addOnFailureListener { e ->
                Log.w("PPTApplication", "There was a problem unsubscribing.", e)
            }

        super.onTerminate()
    }

    private fun setupNotificationChannel() {

        val notificationManager = getSystemService(NotificationManager::class.java)
        val channel =
            NotificationChannel(
                "tracking",
                "Running Notification",
                NotificationManager.IMPORTANCE_LOW
            )
        notificationManager.createNotificationChannel(channel)


        // * Canale dedicato per le notifiche di attività rilevate
        val activityDetectionChannel =
            NotificationChannel(
                "detection_channel",
                "Automatic Tracking Detecting Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Canale dedicato per le notifiche di attività rilevate"
                enableVibration(false)
            }
        notificationManager.createNotificationChannel(activityDetectionChannel)
    }


}