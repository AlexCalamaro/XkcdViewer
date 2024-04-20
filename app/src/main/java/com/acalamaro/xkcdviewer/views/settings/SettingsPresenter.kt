package com.acalamaro.xkcdviewer.views.settings

class SettingsPresenter(
    view: SettingsContract.View,
    model: SettingsContract.Model
): SettingsContract.Presenter {

    private var view: SettingsContract.View? = view
    private var model: SettingsContract.Model? = model

    override fun onViewCreated() {

    }

    override fun loadSettings() {

    }

    override fun onNotificationsToggled() {
        TODO("Not yet implemented")
    }

    override fun onDarkModeImagesToggled() {
        TODO("Not yet implemented")
    }
}