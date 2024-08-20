package it.lam.pptproject.utils

import android.content.Context
import android.util.Log
import it.lam.pptproject.model.room.AppDatabase
import it.lam.pptproject.model.room.TrackingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun saveTrackingData(context: Context) {
    val tracker = Tracker
    Log.d("Tracker", Tracker.toString())
    val trackingData = TrackingData(
        type = tracker.getType(),
        startTime = tracker.getStartTime()!!,
        endTime = tracker.getEndTime()!!,
        values = "",
        steps = tracker.getSteps(),
        username = tracker.getUsername()
    )
    withContext(Dispatchers.IO) {
        Log.d("Tracker", "Saving tracking data into db")
        AppDatabase.getDatabase(context).trackingDataDao().insert(trackingData)
    }
}
