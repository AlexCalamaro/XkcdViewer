package com.acalamaro.xkcdviewer.views.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.prefs.Preferences

internal class SettingsModel(val context: Context): SettingsContract.Model {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SettingsConstants.SETTINGS_STORE_NAME)
    private val notificationToggle = booleanPreferencesKey(SettingsConstants.NOTIFICATIONS_TOGGLE)
    val darkModeImagesToggle = booleanPreferencesKey(SettingsConstants.DARK_MODE_IMAGES_TOGGLE)


    // Preferences setters and getters
    fun getNotificationsToggleState(): Boolean {
        val stateFlow: Flow<Boolean> = context.dataStore.data.map {
            it[notificationToggle] ?: false
        }
    }
    fun setNotificationsToggleState(state: Boolean) {

    }
    fun getDarkModeImagesToggleState(): Boolean {
        return false
    }
    fun setDarkModeImagesToggleState(state: Boolean) {

    }
}