package it.lam.pptproject.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import it.lam.pptproject.R
import it.lam.pptproject.model.room.StatisticsDao
import javax.inject.Inject

const val MINIMUM_STEPS_PER_DAY = 100

@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {


    @Inject
    lateinit var statisticsDao: StatisticsDao

    override fun onReceive(context: Context, intent: Intent) {

        Log.d("DailyReminder", "Received intent")
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = intent.getIntExtra("notificationId", 0)
        var message = intent.getStringExtra("message")
        if(notificationId == 1202){
            val stepsToday = statisticsDao.getTodaySteps(System.currentTimeMillis())
            Log.d("DailyReminder", "Steps today: $stepsToday")
            if(stepsToday < MINIMUM_STEPS_PER_DAY){
                message = context.getString(R.string.daily_rmd_missing_steps, MINIMUM_STEPS_PER_DAY - stepsToday)
            }
        }

        val notification = NotificationCompat.Builder(context, "notification_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Daily Reminder")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        Log.d("Daily_Notification", "Invio notifica giornaliera")
        notificationManager.notify(notificationId, notification)

    }
}