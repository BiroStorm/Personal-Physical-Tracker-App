package it.lam.pptproject.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.ActivityRecognitionResult


// ! NON IN USO
class ActivityDetectionReceiver : BroadcastReceiver() {


    @SuppressLint("QueryPermissionsNeeded")
    override fun onReceive(context: Context, intent: Intent) {

        Log.d(
            "ActivityDetection",
            "Intent chiamato dal DETECTION Receiver}"
        )

        if (ActivityRecognitionResult.hasResult(intent)) {
            val result = ActivityRecognitionResult.extractResult(intent)
            val probact = result?.mostProbableActivity
            probact?.let {

                val info =
                    "Transition: " +
                            probact.toString()
                Log.d("ActivityDetection", info)
            }
        }
    }
}