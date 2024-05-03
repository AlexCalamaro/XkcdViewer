package com.acalamaro.xkcdviewer.views.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private var model: SettingsModel
): ViewModel() {
    private val _uiState = MutableLiveData(SettingsUiModel.EMPTY)
    val uiState: LiveData<SettingsUiModel> = _uiState.distinctUntilChanged() as LiveData<SettingsUiModel>

    init {
        viewModelScope.launch {
            launch {
                model.getDarkModeToggleState().collect {
                    _uiState.value = _uiState.value?.copy(darkModeEnabled = it)
                }
            }
            launch{
                model.getNotificationToggleState().collect {
                    _uiState.value = _uiState.value?.copy(notificationsEnabled = it)
                }
            }
        }
    }
    fun onNotificationsToggled(enabled: Boolean) {
        viewModelScope.launch {
            model.setNotificationsToggleState(enabled)
        }
    }

    fun onDarkModeToggled(enabled: Boolean) {
       viewModelScope.launch {
           model.setDarkModeToggleState(enabled)
       }
    }
}