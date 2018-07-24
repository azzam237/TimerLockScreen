package com.gmail.alhadiq.zamrudi.timerlockscreen.lock_services

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.UserHandle
import android.widget.Toast

class MyAdmin : DeviceAdminReceiver() {
    fun getSamplePreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(
                DeviceAdminReceiver::class.java.name, 0)
    }
    var PREF_PASSWORD_QUALITY = "password_quality"
    var PREF_PASSWORD_LENGTH = "password_length"
    var PREF_MAX_FAILED_PW = "max_failed_pw"

    fun showToast(context: Context, msg: CharSequence) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onEnabled(context: Context, intent: Intent) {
        showToast(context, "Sample Device Admin: enabled")
    }
    //override Charsequence fun onDisabled

    override fun onDisabled(context: Context, intent: Intent) {
        showToast(context, "Sample Device Admin: disabled")
    }

    override fun onPasswordChanged(context: Context, intent: Intent, user: UserHandle) {
        showToast(context, "Sample Device Admin: pw changed")
    }

    override fun onPasswordFailed(context: Context, intent: Intent, user: UserHandle) {
        showToast(context, "Sample Device Admin: pw failed")
    }

    override fun onPasswordSucceeded(context: Context, intent: Intent, user: UserHandle) {
        showToast(context, "Sample Device Admin: pw succeeded")
    }


}
