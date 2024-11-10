package it.lam.pptproject.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.fitness.LocalRecordingClient
import com.google.android.gms.fitness.data.LocalDataType
import com.google.android.gms.fitness.request.LocalDataReadRequest
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.DetectedActivity
import dagger.hilt.android.AndroidEntryPoint
import it.lam.pptproject.data.datastore.DataStoreRepository
import it.lam.pptproject.model.room.AppDatabase
import it.lam.pptproject.model.room.TrackingData
import it.lam.pptproject.utils.ActivityDetectionHelper
import it.lam.pptproject.utils.Utils
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val DETECTED_PENDING_INTENT_REQUEST_CODE = 123
private const val RELIABLE_CONFIDENCE = 70

@AndroidEntryPoint
class DetectedActivityReceiver : BroadcastReceiver() {

    @Inject
    lateinit var database: AppDatabase

    @Inject
    lateinit var dataStore: DataStoreRepository

    @Inject
    lateinit var localRecordingClient: LocalRecordingClient


    override fun onReceive(context: Context, intent: Intent) {
        Log.d("DAR", "Intent di DAR!")
        if (intent.action == "it.lam.pptproject.TerminateDAR") {
            Log.d("DAR", "DAR ha ricevuto il segnato di terminazione!")
            handleTermination()
        }
        if (ActivityRecognitionResult.hasResult(intent)) {
            val result = ActivityRecognitionResult.extractResult(intent)
            result?.let { handleDetectedActivities(it.probableActivities) }
        }
    }

    private fun handleTermination() {
        CoroutineScope(Dispatchers.IO).launch {
            val lastActivity = dataStore.getString("lastActivity") ?: ""
            if (lastActivity.isBlank() || lastActivity.isEmpty()) {
                Log.d("DAR", "Non c'era nessuna attività registrata")
            } else {
                val oldStartTime = dataStore.getStartTime()
                saveIntoDB(lastActivity, oldStartTime)
                dataStore.putString("lastActivity", "")
                dataStore.setStartTime(0)
            }
        }

    }

    private fun handleDetectedActivities(
        detectedActivities: List<DetectedActivity>,
    ) {
        detectedActivities
            .filter {
                it.type == DetectedActivity.STILL ||
                        it.type == DetectedActivity.WALKING ||
                        it.type == DetectedActivity.RUNNING ||
                        it.type == DetectedActivity.IN_VEHICLE ||
                        it.type == DetectedActivity.ON_FOOT
            }
            .filter { it.confidence > RELIABLE_CONFIDENCE }
            .run {
                if (isNotEmpty()) {
                    checkDetectedActivity(this[0])
                    //showNotification(this[0], context)
                }
            }
    }

    private fun checkDetectedActivity(detectedActivity: DetectedActivity) {
        val activity: Utils.RecordType =
            ActivityDetectionHelper.fromActivityType(detectedActivity.type)

        var oldStartTime = 0L
        var lastActivity = ""

        CoroutineScope(Dispatchers.IO).launch {
            oldStartTime = dataStore.getStartTime()
            lastActivity = dataStore.getString("lastActivity") ?: ""
        }.invokeOnCompletion {
            Log.d("DAR", "1 Old Start time: $oldStartTime")
            Log.d("DAR", "2 Detected activity: $activity but last Activity was $lastActivity")

            // * Se l'attività è diversa da quella precedente.
            Log.d(
                "DAR",
                "3 Checking if $activity è uguale a $lastActivity : ${
                    lastActivity.equals(
                        activity.name,
                        true
                    )
                }"
            )
            if (!lastActivity.equals(activity.name, true)) {

                if (lastActivity != "") {
                    // * Il primo caso quando non c'era nessuna attività registrata prima.
                    saveIntoDB(lastActivity, oldStartTime)
                }

                // * Aggiorniamo i valori nel DS.
                CoroutineScope(Dispatchers.IO).launch {
                    Log.d("DAR", "5 Updating DS con last activity: ${activity.name}")
                    dataStore.putString("lastActivity", activity.name)
                    dataStore.setStartTime(System.currentTimeMillis())
                }
            }
        }


    }

    /**
     * A ogni cambio stato, viene salvato il vecchio stato nel Database.
     */
    private fun saveIntoDB(lastActivity: String, oldStartTime: Long) {
        val recordType = Utils.RecordType.valueOf(lastActivity)

        if (recordType == Utils.RecordType.WALKING) {
            CoroutineScope(Dispatchers.IO).launch {
                saveWalking(oldStartTime)
            }.invokeOnCompletion {
                Log.d("DAR", "4A Ha completato il coroutine per saveWalking")
            }
        } else {
            // * Salviamolo normalmente
            CoroutineScope(Dispatchers.IO).launch {
                val newData = TrackingData(
                    type = recordType,
                    startTime = oldStartTime,
                    endTime = System.currentTimeMillis(),
                    values = "",
                    steps = 0,
                    username = dataStore.getString("username")!!
                )
                Log.d("DAR", "4B Not Walking Data Saved: $newData")
                database.trackingDataDao().insert(newData)
            }
        }

    }

    private fun saveWalking(startTime: Long): CompletableDeferred<Unit> {
        val completion = CompletableDeferred<Unit>()
        val endTime = System.currentTimeMillis()
        val readRequest =
            LocalDataReadRequest.Builder()
                .aggregate(LocalDataType.TYPE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build()

        localRecordingClient.readData(readRequest).addOnSuccessListener { response ->
            Log.d("DAR", "Data read success!")

            for (dataSet in response.buckets.flatMap { it.dataSets }) {
                Log.d("FitnessAPI", "Data returned for Data type: ${dataSet.dataType.name}")
                if (dataSet.dataPoints.isEmpty()) {
                    // * Registriamo anche se non sono stati registrati passi.
                    CoroutineScope(Dispatchers.IO).launch {
                        saveDataIntoDB(
                            Utils.RecordType.WALKING,
                            startTime,
                            endTime,
                            "",
                            0
                        )
                    }

                } else {
                    for (dp in dataSet.dataPoints) {
                        Log.d("FitnessAPI", "Data point:")
                        Log.d("FitnessAPI", "\tStart: ${dp.getStartTime(TimeUnit.MILLISECONDS)}")
                        Log.d("FitnessAPI", "\tEnd: ${dp.getEndTime(TimeUnit.MILLISECONDS)}")
                        Log.d("FitnessAPI", "\tValue: ${dp.getValue(dp.dataType.fields[0])}")

                        // * Extraction from data point
                        val startValue = dp.getStartTime(TimeUnit.MILLISECONDS)
                        val endValue = dp.getEndTime(TimeUnit.MILLISECONDS)
                        val steps = dp.getValue(dp.dataType.fields[0]).asInt()

                        // * Saving into DB
                        CoroutineScope(Dispatchers.IO).launch {
                            saveDataIntoDB(
                                Utils.RecordType.WALKING,
                                startValue,
                                endValue,
                                "",
                                steps
                            )
                        }
                    }
                }
            }

        }
        return completion
    }

    private suspend fun saveDataIntoDB(
        walking: Utils.RecordType,
        startValue: Long,
        endValue: Long,
        s: String,
        steps: Int,
    ) {

        val newData = TrackingData(
            type = walking,
            startTime = startValue,
            endTime = endValue,
            values = s,
            steps = steps,
            username = dataStore.getString("username")!!
        )


        Log.d("DAR", "Salvataggio data con steps!: $newData")
        database.trackingDataDao().insert(newData)
    }

    companion object {
        fun getPendingIntent(context: Context): PendingIntent {
            val intent = Intent(context, DetectedActivityReceiver::class.java)
            return PendingIntent.getBroadcast(
                context,
                DETECTED_PENDING_INTENT_REQUEST_CODE,
                intent,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

}