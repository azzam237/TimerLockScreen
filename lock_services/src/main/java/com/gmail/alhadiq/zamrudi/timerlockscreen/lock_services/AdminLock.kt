package com.gmail.alhadiq.zamrudi.timerlockscreen.lock_services

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.SharedPreferences


class AdminLock : DeviceAdminReceiver() {
    fun getSamplePreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(
                DeviceAdminReceiver::class.java.name, 0)
    }

//    fun showToast(context: Context, msg: String) {
//        val status = context.getString(R.string.admin_receiver_status, msg)
//        Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
//    }
//
//    fun onEnabled(context: Context, intent: Intent) {
//        showToast(context, context.getString(R.string.admin_receiver_status_enabled))
//    }
//
//    fun onDisableRequested(context: Context, intent: Intent): CharSequence {
//        return context.getString(R.string.admin_receiver_status_disable_warning)
//    }
//
//    fun onDisabled(context: Context, intent: Intent) {
//        showToast(context, context.getString(R.string.admin_receiver_status_disabled))
//    }
//
//    fun onPasswordChanged(context: Context, intent: Intent) {
//        showToast(context, context.getString(R.string.admin_receiver_status_pw_changed))
//    }
}