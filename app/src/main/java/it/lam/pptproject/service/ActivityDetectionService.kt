package it.lam.pptproject.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityRecognitionClient
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

const val ACTIVITY_UPDATES_INTERVAL = 50L

@AndroidEntryPoint
class ActivityDetectionService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private var startTime = 0L
    private val notificationChannelId = "tracking"
    private lateinit var notificationManager: NotificationManager

    override fun onBind(intent: Intent?): IBinder? = null


    private lateinit var client: ActivityRecognitionClient

    override fun onCreate() {
        super.onCreate()
        client = ActivityRecognition.getClient(this)
        notificationManager = getSystemService(NotificationManager::class.java)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("ActivityDetectionService", "Avviato il Service!")
        when (intent?.action) {
            Actions.START.name -> {
                requestActivityUpdates()
                start()
            }

            Actions.STOP.name -> {
                stopSelf()
            }
        }
        return START_STICKY
    }


    private fun start() {
        startTime = System.currentTimeMillis()
        handler.post(timerRunnable)

        val notification = createNotification("00:00")
        startForeground(1, notification)
    }


    private val timerRunnable = object : Runnable {
        override fun run() {
            val elapsedTime = System.currentTimeMillis() - startTime
            val minutes = (elapsedTime / 1000) / 60
            val seconds = (elapsedTime / 1000) % 60
            val timeText = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

            val notification = createNotification(timeText)
            notificationManager.notify(1, notification)
            handler.postDelayed(this, 1000)
        }
    }

    private fun createNotification(timeText: String): Notification {
        return NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Automatic Tracking")
            .setContentText("Timer: $timeText")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .build()
    }


    @SuppressLint("MissingPermission")
    private fun requestActivityUpdates() {
        val task = client.requestActivityUpdates(
            ACTIVITY_UPDATES_INTERVAL,
            DetectedActivityReceiver.getPendingIntent(this)
        )

        task.run {
            addOnSuccessListener {
                Log.d("ActivityDetectionService", "aggiunto con successo!")
            }
            addOnFailureListener {
                Log.d("ActivityDetectionService", "errore nell'aggiunta!")
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun removeActivityUpdates() {
        try {
            val task = client.removeActivityUpdates(
                DetectedActivityReceiver.getPendingIntent(this)
            )

            task.run {
                addOnSuccessListener {
                    Log.d("ActivityDetectionService", "Rimosso con successo!")
                }
                addOnFailureListener {
                    Log.d("ActivityDetectionService", "errore nella rimozione!")
                }
            }

        } catch (e: Exception) {
            Log.e("ActivityDetectionService", "Errore nella rimozione delle attività", e)
        }
    }

    override fun onDestroy() {
        Log.d("ActivityDetectionService", "Service distrutto!")
        super.onDestroy()
        removeActivityUpdates()
    }


    enum class Actions {
        START, STOP
    }

}