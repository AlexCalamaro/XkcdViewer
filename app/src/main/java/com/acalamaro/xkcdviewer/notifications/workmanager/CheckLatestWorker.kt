package com.acalamaro.xkcdviewer.notifications.workmanager

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.acalamaro.xkcdviewer.data.SettingsDataSource
import com.acalamaro.xkcdviewer.data.XkcdApiDataSource
import com.acalamaro.xkcdviewer.data.remoteobjects.XkcdResult
import com.acalamaro.xkcdviewer.notifications.XkcdNotification
import com.acalamaro.xkcdviewer.notifications.XkcdNotificationPermissions
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.util.UUID

@HiltWorker
class CheckLatestWorker @AssistedInject constructor(
    private val xkcdApiDataSource: XkcdApiDataSource,
    private val settingsDataSource: SettingsDataSource,
    @Assisted val appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        // Check if there is a newer xkcd than the last one we saw
        val lastSeen = settingsDataSource.getNewestKnownXkcdNumber() ?: Int.MAX_VALUE
        val notificationToggleEnabled = settingsDataSource.getNotificationsToggleState().first()
        xkcdApiDataSource.getLatestXkcd().let {
            if (it is XkcdResult.Success) {
                // Set the newest known xkcd number
                settingsDataSource.setNewestKnownXkcdNumber(it.data.num)
                if (it.data.num > lastSeen && notificationToggleEnabled)
                    // Post notification
                    postNotification()
                }
            }
            return Result.success()
        }

    @SuppressLint("MissingPermission")
    private fun postNotification() {
        with(NotificationManagerCompat.from(appContext)) {
            if (XkcdNotificationPermissions.checkPermissions(appContext)) {
                notify(UUID.randomUUID().hashCode(), XkcdNotification.build(appContext))
            }
        }
    }
}