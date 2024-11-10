package it.lam.pptproject.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionEvent
import com.google.android.gms.location.ActivityTransitionResult
import it.lam.pptproject.utils.ActivityDetectionHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


// ! NON IN USO
class ActivityTransitionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("ActivityTransitionReceiver", "Received intent")
        if (ActivityTransitionResult.hasResult(intent)) {

            val result = ActivityTransitionResult.extractResult(intent)
            Log.d("ActivityTransitionReceiver", "$result")
            result?.let { handleTransitionEvents(it.transitionEvents) }
        }
    }

    private fun handleTransitionEvents(transitionEvents: List<ActivityTransitionEvent>) {
        transitionEvents
            // 3
            .filter { it.transitionType == ActivityTransition.ACTIVITY_TRANSITION_ENTER }
            // 4
            .forEach { event : ActivityTransitionEvent ->
                val info =
                    "Transition: " + ActivityDetectionHelper.toActivityString(event.activityType) +
                            " (" + ActivityDetectionHelper.toTransitionType(event.transitionType) + ")" + "  \n" +
                            SimpleDateFormat("HH:mm:ss", Locale.US).format(Date())

                Log.d("ActivityTransition", info)
            }
    }
}