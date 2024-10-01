package it.lam.pptproject.repository

import android.content.Context
import android.util.Log
import androidx.work.WorkManager
import com.google.android.gms.fitness.data.LocalDataSet
import it.lam.pptproject.api.FitnessApiHelper
import it.lam.pptproject.model.room.AppDatabase
import it.lam.pptproject.model.room.TrackingData
import it.lam.pptproject.utils.Tracker
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit


class TrackingRepository(private val context: Context) {

    private val database: AppDatabase = AppDatabase.getDatabase(context)

    fun startTracking() {
        val isWalking = Tracker.getType() == Tracker.RecordType.WALKING
        start(isWalking)
    }

    private fun start(countSteps: Boolean = false) {

        if (countSteps) FitnessApiHelper.startStepCounting(context)

        /*  // ! Worker per salvataggio automatico.
        // Nota: Timer settato a 2 minuti.
        val saveDataWorkRequest = PeriodicWorkRequestBuilder<WalkingWorker>(10, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "TrackingWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            saveDataWorkRequest
        )

         */

        // * Caso in cui non sia Walking.
        Tracker.start()
    }

    suspend fun endTracking(){

        val isWalking = Tracker.getType() == Tracker.RecordType.WALKING
        if (isWalking) {
            saveWalkingData()
        }else{
            Tracker.stop()
            saveTrackingData()
        }
        // ! Possibile problema di sync. Se viene terminato prima del salvataggio.
        FitnessApiHelper.stopStepCounting(context)

        // WorkManager.getInstance(context).cancelUniqueWork("TrackingWorker")
    }

    private suspend fun saveTrackingData() {

        Tracker.setSteps(0)
        database.trackingDataDao().insert(Tracker.convertToTrackingData())

    }

    private suspend fun saveWalkingData() {
        val stepList: List<LocalDataSet> = retrieveStepList()
        for (dataSet in stepList) {
            if(dataSet.dataPoints.isEmpty()){
                // * Nel caso in cui non siano stati registrati steps...
                // * Registrare solamente i dati di inizio e fine con step 0.
                Tracker.stop()
                saveTrackingData()
            }else {
                // * Nel caso in cui ci siano dati da salvare...
                for (dp in dataSet.dataPoints) {
                    // * Nota: In realtà dataSet.dataPoints contiene solo 1 elemento.
                    Log.d(
                        "TrackingRepository",
                        "Saving Data...\n" +
                                "Start_Time: ${dp.getStartTime(TimeUnit.MILLISECONDS)}" +
                                "\nEnd_Time: ${dp.getEndTime(TimeUnit.MILLISECONDS)}" +
                                "\nSteps: ${dp.getValue(dp.dataType.fields[0]).asInt()}"
                    )
                    val td = TrackingData(
                        type = Tracker.RecordType.WALKING,
                        startTime = dp.getStartTime(TimeUnit.MILLISECONDS),
                        endTime = dp.getEndTime(TimeUnit.MILLISECONDS),
                        values = "",
                        steps = dp.getValue(dp.dataType.fields[0]).asInt(),
                        username = Tracker.getUsername()
                    )
                    database.trackingDataDao().insert(td)
                    // * Si fa in caso segua un dataset vuoto:
                    Tracker.setStartTime(dp.getEndTime(TimeUnit.MILLISECONDS).plus(1))
                }
            }
        }

    }


    private suspend fun retrieveStepList(): List<LocalDataSet> {
        // Logica per recuperare i passi, ad esempio da FitnessApiHelper
        val deferredSteps = CompletableDeferred<List<LocalDataSet>>()

        FitnessApiHelper.getLastStepCount(context) { steps ->
            deferredSteps.complete(steps)
        }
        return withContext(Dispatchers.IO) {
            deferredSteps.await()
        }
    }


    suspend fun printDati(){
        Log.d("TrackingRepository", "printDati")
        val stepList: List<LocalDataSet> = retrieveStepList()

        for (dataSet in stepList) {
            Log.d("TrackingRepository", "Lettura dati dal dataset")
            if(dataSet.dataPoints.isEmpty()){
                Log.d("TrackingRepository", "Empty dataset")
            }else {
                for (dp in dataSet.dataPoints) {
                    // * Nel caso in cui ci siano dati da salvare...
                    Log.d(
                        "TrackingRepository",
                        "Saving Data...\nStart_Time: ${dp.getStartTime(TimeUnit.MILLISECONDS)}" +
                                "\nEnd_Time: ${dp.getEndTime(TimeUnit.MILLISECONDS)}" +
                                "\nSteps: ${dp.getValue(dp.dataType.fields[0]).asInt()}"
                    )
                }
            }
        }
    }

}