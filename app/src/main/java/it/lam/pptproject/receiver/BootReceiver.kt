package it.lam.pptproject.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import it.lam.pptproject.R
import it.lam.pptproject.utils.Utils.scheduleDailyNotification


class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("BootReceiver", "Avvio scheduling notifiche giornaliere")
            scheduleDailyNotification(
                context = context,
                hour = 9,
                minute = 0,
                notificationId = 1201,
                message = context.getString(R.string.daily_rmd_start)
            )
            scheduleDailyNotification(
                context = context,
                hour = 18,
                minute = 0,
                notificationId = 1202,
                message = context.getString(R.string.daily_rmd_evening)
            )
        }
    }
}
