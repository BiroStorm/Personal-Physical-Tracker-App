package it.lam.pptproject.service

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionEvent
import com.google.android.gms.location.DetectedActivity
import it.lam.pptproject.utils.ActivityDetectionHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


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



            //result?.let { handleTransitionEvents(it.transitionEvents) }


            /*
                        result?.let {

                            it.probableActivities.forEach { event : DetectedActivity ->
                                val info =
                                    "Transition: " + ActivityDetectionHelper.toActivityString(event.type) +
                                            " (" + ActivityDetectionHelper.toTransitionType(event.type) + ")" + "  \n" +
                                            SimpleDateFormat("HH:mm:ss", Locale.US).format(Date())
                                Log.d("ActivityDetection", info)
                            }
                            handleDetectedActivities(it.probableActivities, context)

                        }
             */
        }
    }


    /*

    companion object {

        fun getPendingIntent(context: Context): PendingIntent {
            val intent = Intent(context, ActivityDetectionReceiver::class.java)
            return PendingIntent.getBroadcast(
                context, DETECTED_PENDING_INTENT_REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }



    private fun showNotification(detectedActivity: DetectedActivity, context: Context) {
        createNotificationChannel(context)
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra(
                SUPPORTED_ACTIVITY_KEY,
                SupportedActivity.fromActivityType(detectedActivity.type)
            )
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val activity = SupportedActivity.fromActivityType(detectedActivity.type)

        val builder = NotificationCompat.Builder(context, DETECTED_ACTIVITY_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(activity.activityText))
            .setContentText("Your pet is ${detectedActivity.confidence}% sure of it")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(DETECTED_ACTIVITY_NOTIFICATION_ID, builder.build())
        }
    }

    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "detected_activity_channel_name"
            val descriptionText = "detected_activity_channel_description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(DETECTED_ACTIVITY_CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                    enableVibration(false)
                }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

     */
}