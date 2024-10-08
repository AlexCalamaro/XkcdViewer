package com.squidink.xkcdviewer.notifications

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.squidink.xkcdviewer.views.main.MainActivity

object XkcdNotificationPermissions {

    const val PERMISSION_REQUEST_CODE = 12391

    /**
     * Check if POST_NOTICIATION permission is granted, request permissions if not granted
     * @return true if granted, false otherwise
     */
    fun checkRequestPermissions(activity: MainActivity): Boolean {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    PERMISSION_REQUEST_CODE
                )
                return false
            }
        }
        return true
    }

    /**
     * Check if POST_NOTICIATION permission is granted
     * @return true if granted, false otherwise
     */
    fun checkPermissions(context: Context): Boolean {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
}