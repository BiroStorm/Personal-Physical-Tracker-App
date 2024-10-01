package it.lam.pptproject.api

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.Fitness.getHistoryClient
import com.google.android.gms.fitness.FitnessLocal
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.LocalDataSet
import com.google.android.gms.fitness.data.LocalDataType
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.request.LocalDataReadRequest
import com.google.android.gms.fitness.request.SensorRequest
import it.lam.pptproject.R
import it.lam.pptproject.api.FitnessAPI.dumpDataSet
import java.util.concurrent.TimeUnit

object FitnessApiHelper {



    // Metodo per richiedere i permessi a Google Fit
    fun requestFitnessPermissions(context: Context, activity: Activity, requestCode: Int) {
        val fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .build()
        // TODO REQUEST PERMISSIONS

    }

    // Metodo per iniziare a contare i passi
    @SuppressLint("MissingPermission")
    fun startStepCounting(context: Context) {
        val localRecordingClient = FitnessLocal.getLocalRecordingClient(context)

        localRecordingClient.subscribe(LocalDataType.TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener {
                Log.i("FitnessAPI", "Successfully subscribed!")
            }
            .addOnFailureListener { e ->
                Log.w("FitnessAPI", "There was a problem subscribing.", e)
            }

    }

    // Ascolta gli aggiornamenti sui passi
    private fun listenToStepCount(context: Context, onStepsUpdated: (Int) -> Unit) {
        val stepRequest = SensorRequest.Builder()
            .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
            .setSamplingRate(1, TimeUnit.SECONDS)
            .build()

        Log.d("FitnessHelper", "non so cosa succede....")
    }

    // Metodo per interrompere la registrazione dei passi, se necessario
    fun stopStepCounting(context: Context) {
        val localRecordingClient = FitnessLocal.getLocalRecordingClient(context)
        localRecordingClient.unsubscribe(LocalDataType.TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener {
                Log.i("FitnessAPI", "Successfully unsubscribed!")
            }
            .addOnFailureListener { e ->
                Log.w("FitnessAPI", "There was a problem unsubscribing.", e)
            }
    }



    fun getLastStepCount(context: Context, onResult: (List<LocalDataSet>) -> Unit) {

        val localRecordingClient = FitnessLocal.getLocalRecordingClient(context)


        // DEBUG: TODO Need to Change
        val endTime = System.currentTimeMillis() // Tempo corrente
        val startTime = endTime - 24 * 60 * 60 * 1000 // Ultimo giorno
        // todo: settare un timer iniziale, partendo da un orario rounded (10:00, 11:00)


        val readRequest =
            LocalDataReadRequest.Builder()
                .aggregate(LocalDataType.TYPE_STEP_COUNT_DELTA)
                // MODIFICARE QUA PER MODIFICARE IL TEMPO DI RAGGRUPPAMENTO.
                .bucketByTime(2, TimeUnit.MINUTES)
                 .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)

        localRecordingClient.readData(readRequest.build())
            .addOnSuccessListener { response ->
                onResult(response.buckets.flatMap { it.dataSets })

            }
            .addOnFailureListener { e ->
                Log.w("FitnessAPI", "There was a problem reading the step count.", e)
                onResult(emptyList())
            }
    }
}
