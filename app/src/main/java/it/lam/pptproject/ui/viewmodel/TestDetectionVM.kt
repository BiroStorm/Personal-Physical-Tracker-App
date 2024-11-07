package it.lam.pptproject.ui.viewmodel

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityRecognitionClient
import com.google.android.gms.location.ActivityTransitionRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import it.lam.pptproject.service.ActivityDetectionReceiver
import it.lam.pptproject.service.ActivityTransitionReceiver
import it.lam.pptproject.utils.ActivityDetectionHelper
import javax.inject.Inject

@HiltViewModel
class TestDetectionVM @Inject constructor(
    @ApplicationContext private val appContext: Context,
) : ViewModel() {

    var client: ActivityRecognitionClient = ActivityRecognition.getClient(appContext)
    val REQUEST_CODE_INTENT_ACTIVITY_TRANSITION = 122


    @SuppressLint("MissingPermission")
    private fun deregisterForUpdates() {
        client.removeActivityTransitionUpdates(getPendingIntent())
            .addOnSuccessListener {
                Log.d("TestDetectionVM", "Rimosso con successo")
                getPendingIntent().cancel()

            }
            .addOnFailureListener { e: Exception ->
                Log.e("TestDetectionVM", "Failed to remove activity updates: $e")
            }
    }


    var isRegistered = false

    @SuppressLint("MissingPermission")
    fun clickOnButton() {
        isRegistered = !isRegistered
        Log.d("TestDetectionVM", "new isRegistered: $isRegistered")
        if (!isRegistered) {
            deregisterForUpdates()
        } else {

            val request = ActivityTransitionRequest(ActivityDetectionHelper.getTransitions())


            client.requestActivityTransitionUpdates(
                request,
                getPendingIntent()
            )
                .addOnSuccessListener {
                    Log.d("TestDetectionVM", "Aggiunto con successo")
                }
                .addOnFailureListener {
                    Log.d("TestDetectionVM", "addOnFailureListener")
                }


/*
            val broadCastIntent = Intent(appContext, ActivityDetectionReceiver::class.java)
            client.requestActivityUpdates(
                100,
                PendingIntent.getBroadcast(
                    appContext,
                    82,
                    broadCastIntent,
                    PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
            )

 */

        }


    }


    private fun getPendingIntent(): PendingIntent {
        Log.d("TestDetectionVM", "getPendingIntent")
        val intent = Intent(appContext, ActivityTransitionReceiver::class.java)

        return PendingIntent.getBroadcast(
            appContext,
            86,
            intent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}