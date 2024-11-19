package it.lam.pptproject.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import it.lam.pptproject.receiver.NotificationReceiver
import java.util.Calendar

object Utils {

    enum class RecordType {
        WALKING, DRIVING, SITTING, AUTOMATIC
    }

    fun scheduleDailyNotification(context: Context, hour: Int, minute: Int, notificationId: Int, message: String) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("notificationId", notificationId)
            putExtra("message", message)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            // si controlla se è l'orario è già passato, se è passato si setta per il giorno successivo.
            if (before(Calendar.getInstance())) add(Calendar.DAY_OF_MONTH, 1)
        }

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        Log.d("Daily_Notification", "Scheduled notification for $hour:$minute")
    }


}