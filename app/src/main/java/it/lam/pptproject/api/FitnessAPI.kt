package it.lam.pptproject.api

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.fitness.FitnessLocal
import com.google.android.gms.fitness.data.LocalDataSet
import com.google.android.gms.fitness.data.LocalDataType
import com.google.android.gms.fitness.request.LocalDataReadRequest
import it.lam.pptproject.utils.Tracker
import it.lam.pptproject.utils.saveTrackingData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit


object FitnessAPI{
    @SuppressLint("MissingPermission")
    fun subscribeToFitnessData(context: Context) {
        val localRecordingClient = FitnessLocal.getLocalRecordingClient(context)

        localRecordingClient.subscribe(LocalDataType.TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener {
                Log.i("FitnessAPI", "Successfully subscribed!")
            }
            .addOnFailureListener { e ->
                Log.w("FitnessAPI", "There was a problem subscribing.", e)
            }
    }

    fun unsubscribeFromFitnessData(context: Context) {
        val localRecordingClient = FitnessLocal.getLocalRecordingClient(context)
        localRecordingClient.unsubscribe(LocalDataType.TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener {
                Log.i("FitnessAPI", "Successfully unsubscribed!")
            }
            .addOnFailureListener { e ->
                Log.w("FitnessAPI", "There was a problem unsubscribing.", e)
            }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun saveData(context: Context) {
        val localRecordingClient = FitnessLocal.getLocalRecordingClient(context)
        val endTime = LocalDateTime.now().atZone(ZoneId.systemDefault())
        val startTime = endTime.minusWeeks(1)
        val readRequest =
            LocalDataReadRequest.Builder()
                // The data request can specify multiple data types to return,
                // effectively combining multiple data queries into one call.
                // This example demonstrates aggregating only one data type.
                .aggregate(LocalDataType.TYPE_STEP_COUNT_DELTA)
                // Analogous to a "Group By" in SQL, defines how data should be
                // aggregated. bucketByTime allows bucketing by time span.
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime.toEpochSecond(), endTime.toEpochSecond(), TimeUnit.SECONDS)
                .build()

        localRecordingClient.readData(readRequest).addOnSuccessListener { response ->
            // The aggregate query puts datasets into buckets, so flatten into a
            // single list of datasets.
            for (dataSet in response.buckets.flatMap { it.dataSets }) {
                //dumpDataSet(dataSet)
                Log.d("FitnessAPI", "Data returned for Data type: ${dataSet.dataType.name}")
                for (dp in dataSet.dataPoints) {
                    Log.d("FitnessAPI","Data point:")
                    Log.d("FitnessAPI","\tStart: ${dp.getStartTime(TimeUnit.MILLISECONDS)}")
                    Log.d("FitnessAPI","\tEnd: ${dp.getEndTime(TimeUnit.MILLISECONDS)}")
                    Log.d("FitnessAPI", "\tValue: ${dp.getValue(dp.dataType.fields[0])}")
                    Tracker.setStartTime(dp.getStartTime(TimeUnit.MILLISECONDS))
                    Tracker.setEndTime(dp.getEndTime(TimeUnit.MILLISECONDS))
                    Tracker.setSteps(dp.getValue(dp.dataType.fields[0]).asInt())

                    // call saveTrackingData in coroutine

                }
                // ! IN QUESTO CASO PRENDE SOLO L'ULTIMO DATASET (todo, metterlo nel for)
                CoroutineScope(Dispatchers.IO).launch {
                    saveTrackingData(context)
                    Tracker.clearData()
                }
            }
        }
            .addOnFailureListener { e ->
                Log.w("FitnessAPI","There was an error reading data", e)
            }
        
    }

    fun dumpDataSet(dataSet: LocalDataSet) {
        Log.i("FitnessAPI", "Data returned for Data type: ${dataSet.dataType.name}")
        for (dp in dataSet.dataPoints) {
            Log.i("FitnessAPI","Data point:")
            Log.i("FitnessAPI","\tType: ${dp.dataType.name}")
            Log.i("FitnessAPI","\tStart: ${dp.getStartTime(TimeUnit.MILLISECONDS)}")
            Log.i("FitnessAPI","\tEnd: ${dp.getEndTime(TimeUnit.MILLISECONDS)}")
            for (field in dp.dataType.fields) {
                Log.i("FitnessAPI","\tLocalField: ${field.name.toString()} LocalValue: ${dp.getValue(field)}")
            }
        }
    }
}


