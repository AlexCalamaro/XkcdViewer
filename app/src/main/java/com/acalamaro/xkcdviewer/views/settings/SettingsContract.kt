package com.acalamaro.xkcdviewer.views.settings

interface SettingsContract {

    interface View {
        fun setNotificationsToggleState()
        fun setDarkModeImagesToggleState()
    }

    interface Presenter {
        fun onViewCreated()
        fun loadSettings()
        fun onNotificationsToggled()
        fun onDarkModeImagesToggled()
    }

    interface Model {

    }
}