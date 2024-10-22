package it.lam.pptproject.service

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.fitness.data.LocalDataSet
import it.lam.pptproject.api.FitnessApiHelper
import it.lam.pptproject.repository.TrackingRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WalkingWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        Log.d("WalkingWorker", "Working!!!!!!!!")
        // Recupera i dati dei passi dalla Fitness API
        val steps = retrieveSteps()

        //val repository = TrackingRepository(applicationContext)


        //repository.saveTrackingData(steps)

        return Result.success()
    }

    private suspend fun retrieveSteps(): List<LocalDataSet> {
        // Logica per recuperare i passi, ad esempio da FitnessApiHelper
        val deferredSteps = CompletableDeferred<List<LocalDataSet>>()

        FitnessApiHelper.getLastStepCount(applicationContext) { steps ->
            deferredSteps.complete(steps)
        }

        return withContext(Dispatchers.IO) {
            deferredSteps.await()
        }
    }
}
