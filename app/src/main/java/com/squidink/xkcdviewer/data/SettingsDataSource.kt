package com.squidink.xkcdviewer.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.squidink.xkcdviewer.views.settings.SettingsConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
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
    private val newestKnownXkcdNumber = intPreferencesKey(SettingsConstants.NEWEST_KNOWN_XKCD_NUMBER)
    private val isFirstLaunch = booleanPreferencesKey(SettingsConstants.IS_FIRST_LAUNCH)

    fun getNotificationsToggleState(): Flow<Boolean> {
        return context.dataStore.data.map {
            it[notificationToggle] ?: false
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

    suspend fun getNewestKnownXkcdNumber(): Int? {
        return context.dataStore.data.map {
            it[newestKnownXkcdNumber] ?: 0
        }.firstOrNull()
    }

    suspend fun setNewestKnownXkcdNumber(number: Int) {
        context.dataStore.edit {
            it[newestKnownXkcdNumber] = number
        }
    }

    // Determines whether app has been previously launched
    suspend fun getIsFirstAppLaunch(): Boolean {
        val result = context.dataStore.data.firstOrNull()?.get(isFirstLaunch) ?: true

        if(result) {
            context.dataStore.edit {
                it[isFirstLaunch] = false
            }
        }
        return result
    }
}