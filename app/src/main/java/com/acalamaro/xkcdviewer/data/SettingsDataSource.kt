package com.acalamaro.xkcdviewer.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.acalamaro.xkcdviewer.views.settings.SettingsConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = SettingsConstants.SETTINGS_STORE_NAME
)
@Singleton
class SettingsDataSource @Inject constructor(private val context: Context) {
    private val notificationToggle = booleanPreferencesKey(SettingsConstants.NOTIFICATIONS_TOGGLE)
    private val darkModeImagesToggle = booleanPreferencesKey(SettingsConstants.DARK_MODE_TOGGLE)

    fun getNotificationsToggleState(): Flow<Boolean> {
        return context.dataStore.data.map {
            it[notificationToggle] ?: true
        }
    }

    suspend fun setNotificationsToggleState(state: Boolean) {
        context.dataStore.edit {
            it[notificationToggle] = state
        }
    }

    fun getDarkModeToggleState(): Flow<Boolean> {
        return context.dataStore.data.map {
            it[darkModeImagesToggle] ?: true
        }
    }

    suspend fun setDarkModeToggleState(state: Boolean) {
        context.dataStore.edit {
            it[darkModeImagesToggle] = state
        }
    }
}