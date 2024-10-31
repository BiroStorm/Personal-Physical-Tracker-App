package it.lam.pptproject.service

import android.app.ForegroundServiceStartNotAllowedException
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat

class TrackingService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when (intent?.action) {
            Actions.START.name -> start()
            Actions.STOP.name -> stopSelf()
        }

        return START_STICKY
    }


    private fun start() {
        val notification = NotificationCompat.Builder(this, "tracking")
            .setContentTitle("Tracking")
            .setContentText("Tracking in progress da ...")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .build()

        startForeground(1, notification)
    }

    override fun onCreate() {
        // da aggiungere qualcosa... forse....
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    enum class Actions {
        START, STOP
    }
}