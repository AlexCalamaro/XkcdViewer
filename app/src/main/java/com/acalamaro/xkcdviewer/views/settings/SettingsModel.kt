package com.acalamaro.xkcdviewer.views.settings

import com.acalamaro.xkcdviewer.data.SettingsDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsModel @Inject constructor(
    private var dataSource: SettingsDataSource
) {
    fun getNotificationToggleState(): Flow<Boolean> {
        return dataSource.getNotificationsToggleState()
    }
    fun getDarkModeToggleState(): Flow<Boolean> {
        return dataSource.getDarkModeToggleState()
    }
    suspend fun setNotificationsToggleState(state: Boolean) {
        dataSource.setNotificationsToggleState(state)
    }
    suspend fun setDarkModeToggleState(state: Boolean) {
        dataSource.setDarkModeToggleState(state)
    }
}