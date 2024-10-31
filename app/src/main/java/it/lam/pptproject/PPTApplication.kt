package it.lam.pptproject

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import it.lam.pptproject.data.AppContainer
import it.lam.pptproject.data.AppDataContainer

// DO NOT TOUCH ! used by Hilt.
@HiltAndroidApp
class PPTApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)

        val channel =
            NotificationChannel(
                "tracking",
                "Running Notification",
                NotificationManager.IMPORTANCE_LOW)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }


}