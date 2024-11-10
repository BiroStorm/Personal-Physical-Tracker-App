package it.lam.pptproject

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.IntentFilter
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.fitness.FitnessLocal
import com.google.android.gms.fitness.LocalRecordingClient
import com.google.android.gms.fitness.data.LocalDataType
import dagger.hilt.android.HiltAndroidApp
import it.lam.pptproject.data.datastore.DataStoreRepository
import it.lam.pptproject.receiver.DetectedActivityReceiver
import it.lam.pptproject.utils.Utils.scheduleDailyNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class PPTApplication : Application() {

    private lateinit var localRecordingClient: LocalRecordingClient

    private val detectedActivityReceiver = DetectedActivityReceiver()

    @Inject
    lateinit var dataStoreRepository: DataStoreRepository

    @SuppressLint("InlinedApi")
    override fun onCreate() {
        super.onCreate()

        // * Initialize the DataStore
        CoroutineScope(Dispatchers.IO).launch {
            dataStoreRepository.initializeDefaults()
        }

        // * Initialize the Notification Channel
        setupNotificationChannel()

        executeScheduledNotifications()

        // Perform a Play Services version check
        val hasMinPlayServices = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
            this,
            LocalRecordingClient.LOCAL_RECORDING_CLIENT_MIN_VERSION_CODE
        )
        if (hasMinPlayServices != ConnectionResult.SUCCESS) {
            Log.w("MainActivity", "Google Play Services troppo vecchia!")
        }

        localRecordingClient = FitnessLocal.getLocalRecordingClient(this)

        // * Registrazione del BroadcastReceiver, necessario per attivare il BroadcastReceiver prima.
        val filter = IntentFilter("it.lam.pptproject.TerminateDAR")
        registerReceiver(detectedActivityReceiver, filter, RECEIVER_NOT_EXPORTED)

    }

    private fun executeScheduledNotifications() {
        scheduleDailyNotification(
            context = this,
            hour = 9,
            minute = 0,
            notificationId = 1201,
            message = getString(R.string.daily_rmd_start)
        )

        scheduleDailyNotification(
            context = this,
            hour = 18,
            minute = 0,
            notificationId = 1202,
            message = getString(R.string.daily_rmd_evening)
        )
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

        // * Dedicato per le notifiche di inizio e fine attività scelte dall'utente
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


        val activityNotificationChannel =
            NotificationChannel(
                "notification_channel",
                "Daily Notification Channel",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canale dedicato per le notifiche giornaliere"
                enableVibration(true)
            }
        notificationManager.createNotificationChannel(activityNotificationChannel)
    }


}