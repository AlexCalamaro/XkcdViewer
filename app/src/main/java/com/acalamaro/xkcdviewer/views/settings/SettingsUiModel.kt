package com.acalamaro.xkcdviewer.views.settings

data class SettingsUiModel (
    val notificationsEnabled: Boolean,
    val darkModeEnabled: Boolean
) {
    companion object {
        val EMPTY = SettingsUiModel(
            notificationsEnabled = false,
            darkModeEnabled = false
        )
    }
}