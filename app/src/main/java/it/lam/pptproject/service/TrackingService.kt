package it.lam.pptproject.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.fitness.LocalRecordingClient
import com.google.android.gms.fitness.data.LocalDataType
import com.google.android.gms.fitness.request.LocalDataReadRequest
import dagger.hilt.android.AndroidEntryPoint
import it.lam.pptproject.data.datastore.DataStoreRepository
import it.lam.pptproject.model.room.AppDatabase
import it.lam.pptproject.model.room.TrackingData
import it.lam.pptproject.utils.Utils
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class TrackingService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private var startTime = 0L
    private val notificationChannelId = "tracking"
    private lateinit var notificationManager: NotificationManager
    private lateinit var trackingType: Utils.RecordType

    @Inject
    lateinit var database: AppDatabase

    @Inject
    lateinit var dataStore: DataStoreRepository

    @Inject
    lateinit var localRecordingClient: LocalRecordingClient

    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(NotificationManager::class.java)

    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.extras?.getString("selectedOption")?.let {
            trackingType = Utils.RecordType.valueOf(it)
        }
        when (intent?.action) {
            Actions.START.name -> start()
            Actions.STOP.name -> stopSelf()
        }
        return START_STICKY
    }

    private fun start() {
        startTime = System.currentTimeMillis()
        handler.post(timerRunnable)

        val notification = createNotification("00:00")
        startForeground(1, notification)
    }

    private val timerRunnable = object : Runnable {
        override fun run() {
            val elapsedTime = System.currentTimeMillis() - startTime
            val minutes = (elapsedTime / 1000) / 60
            val seconds = (elapsedTime / 1000) % 60
            val timeText = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

            updateNotification(timeText)
            handler.postDelayed(this, 1000)  // Aggiornamento ogni secondo
        }
    }

    private fun createNotification(timeText: String): Notification {
        return NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Tracking $trackingType")
            .setContentText("Timer: $timeText")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .build()
    }

    private fun updateNotification(timeText: String) {
        val notification = createNotification(timeText)
        notificationManager.notify(1, notification)
    }

    override fun onDestroy() {
        handler.removeCallbacks(timerRunnable)
        val isWalking = trackingType == Utils.RecordType.WALKING

        serviceScope.launch {

            if (isWalking) {
                try {
                    getSteps().await()  // Aspetta che `getSteps` completi l'inserimento
                } catch (e: Exception) {
                    Log.e("TrackingService", "Error during getSteps execution", e)
                }
            } else {
                saveDataIntoDB()
            }

        }.invokeOnCompletion {
            Log.i("TrackingService", "Cancelling serviceScope...")
            serviceScope.cancel()
        }

        Log.i("TrackingService", "Service destroyed")
        super.onDestroy()
    }

    private suspend fun saveDataIntoDB() {
        val newData = TrackingData(
            type = trackingType,
            startTime = startTime,
            endTime = System.currentTimeMillis(),
            values = "",
            steps = 0,
            username = dataStore.getString("username")!!
        )

        Log.i("TrackingService", "Data to save: $newData")

        database.trackingDataDao().insert(newData)
        Log.i("TrackingService", "Data saved into DB")
    }

    private suspend fun saveDataIntoDB(
        type: Utils.RecordType,
        startTime: Long,
        endTime: Long,
        values: String = "",
        steps: Int = 0,
    ) {
        Log.i("TrackingService", "saveDataIntoDB() con parametri 2")
        val newData = TrackingData(
            type = type,
            startTime = startTime,
            endTime = endTime,
            values = values,
            steps = steps,
            username = dataStore.getString("username")!!
        )

        Log.i("TrackingService", "Data to save: $newData")

        database.trackingDataDao().insert(newData)
        Log.i("TrackingService", "Data saved into DB")
    }

    private fun getSteps(): CompletableDeferred<Unit> {

        val completion = CompletableDeferred<Unit>()

        val endTime = System.currentTimeMillis()
        val readRequest =
            LocalDataReadRequest.Builder()
                .aggregate(LocalDataType.TYPE_STEP_COUNT_DELTA)
                .bucketByTime(30, TimeUnit.SECONDS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build()

        localRecordingClient.readData(readRequest).addOnSuccessListener { response ->

            val jobs = mutableListOf<Job>()

            for (dataSet in response.buckets.flatMap { it.dataSets }) {
                Log.d("FitnessAPI", "Data returned for Data type: ${dataSet.dataType.name}")
                for (dp in dataSet.dataPoints) {
                    Log.d("FitnessAPI", "Data point:")
                    Log.d("FitnessAPI", "\tStart: ${dp.getStartTime(TimeUnit.MILLISECONDS)}")
                    Log.d("FitnessAPI", "\tEnd: ${dp.getEndTime(TimeUnit.MILLISECONDS)}")
                    Log.d("FitnessAPI", "\tValue: ${dp.getValue(dp.dataType.fields[0])}")

                    // * Extraction from data point
                    val startValue = dp.getStartTime(TimeUnit.MILLISECONDS)
                    val endValue = dp.getEndTime(TimeUnit.MILLISECONDS)
                    val steps = dp.getValue(dp.dataType.fields[0]).asInt()

                    // * Saving into DB.
                    val job = serviceScope.launch {
                        saveDataIntoDB(
                            Utils.RecordType.WALKING,
                            startValue,
                            endValue,
                            "",
                            steps
                        )
                    }
                    jobs.add(job)
                }

            }

            serviceScope.launch {
                jobs.joinAll()
                completion.complete(Unit)
            }
        }
            .addOnFailureListener { e ->
                Log.w("TrackingService", "There was an error reading data in [getSteps()]", e)
                completion.completeExceptionally(e)
            }

        return completion
    }


    enum class Actions {
        START, STOP
    }
}