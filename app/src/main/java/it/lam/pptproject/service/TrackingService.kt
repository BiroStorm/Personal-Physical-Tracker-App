package it.lam.pptproject.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import java.util.Locale

class TrackingService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private var startTime = 0L
    private val notificationChannelId = "tracking"
    private lateinit var notificationManager: NotificationManager
    private var trackingType = ""

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(NotificationManager::class.java)

    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.extras?.getString("selectedOption")?.let {
            trackingType = it
        }
        when (intent?.action) {
            Actions.START.name -> start()
            Actions.STOP.name -> stopSelf()
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

            updateNotification(timeText)
            handler.postDelayed(this, 1000)  // Aggiornamento ogni secondo
        }
    }

    private fun createNotification(timeText: String): Notification {
        return NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Tracking $trackingType")
            .setContentText("Tempo trascorso: $timeText")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .build()
    }
    private fun updateNotification(timeText: String) {
        val notification = createNotification(timeText)
        notificationManager.notify(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timerRunnable)
    }

    enum class Actions {
        START, STOP
    }
}