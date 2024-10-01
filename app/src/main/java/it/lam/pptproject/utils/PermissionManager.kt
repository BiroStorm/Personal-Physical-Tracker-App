package it.lam.pptproject.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import it.lam.pptproject.R

object PermissionManager {

    // * Forza l'utente a modificare le impostazioni dell'app per concedere i permessi necessari
    fun forcePermissionRequest(context: Activity) {
        AlertDialog.Builder(context)
            .setTitle("Permission Required")
            .setMessage(R.string.force_permission_txt)
            .setPositiveButton(R.string.settings) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)}
            .setNegativeButton("Cancel") { _, _ ->
                context.finish()
            }
            .create()
            .show()
    }
}