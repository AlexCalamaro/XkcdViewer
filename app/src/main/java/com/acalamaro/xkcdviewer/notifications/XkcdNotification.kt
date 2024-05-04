package com.acalamaro.xkcdviewer.notifications

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import com.acalamaro.xkcdviewer.R
import com.acalamaro.xkcdviewer.extensions.getRandom
import com.acalamaro.xkcdviewer.views.main.MainActivity

object XkcdNotification {

    fun build(context: Context): Notification {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val bodyArray = context.resources.getStringArray(R.array.notification_content)

        val builder = NotificationCompat.Builder(
            context,
            context.getString(R.string.notification_channel_id)
        )
            .setSmallIcon(R.drawable.xkcd_hat_alpha)
            .setLargeIcon(AppCompatResources.getDrawable(context, R.drawable.xkcd_hat)!!.toBitmap())
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(bodyArray.getRandom())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        return builder.build()
    }
}