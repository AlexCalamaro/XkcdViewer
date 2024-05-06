package com.acalamaro.xkcdviewer.views.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.acalamaro.xkcdviewer.data.remoteobjects.XkcdResponse
import com.acalamaro.xkcdviewer.data.remoteobjects.XkcdResult
import com.acalamaro.xkcdviewer.extensions.blankIfNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val model: MainModel,
): ViewModel() {

    private val _uiState = MutableLiveData<MainUiModel>()
    val uiState: LiveData<MainUiModel> = _uiState as LiveData<MainUiModel>

    fun loadLatest() {
        loadXkcd(null)
    }

    fun loadNext() {
        _uiState.value?.let {
            when(it.number) {
                null -> loadXkcd(null)
                it.newestNumber -> loadXkcd(1)
                else -> loadXkcd(it.number+1)
            }
        }
    }

    fun loadPrevious() {
        _uiState.value?.let {
            when(it.number) {
                null, 1 -> loadXkcd(null)
                else -> loadXkcd(it.number-1)
            }
        }
    }

    fun loadRandom() {
        _uiState.value?.let {
            when(it.newestNumber) {
                null -> loadXkcd(null)
                else -> loadXkcd((1..it.newestNumber).random())
            }
        }
    }

    fun loadSpecific(selected: Int) {
        _uiState.value?.let {
            if(it.newestNumber == null) {
                loadXkcd(null)
            } else if(selected in (1..it.newestNumber)) {
                loadXkcd(selected)
            } else {
                loadXkcd(null)
            }
        }
    }
    private fun loadXkcd(number : Int?) {
        CoroutineScope(Dispatchers.IO).launch {
            model?.loadXkcd(number)?.let {
                when(it) {
                    is XkcdResult.Success -> {
                        withContext(Dispatchers.Main) {
                            applyResponseToState(it.data)

                            // When loading the newest comic, update the newest known number
                            // in the settings to support notification system
                            if(number == null) {
                                model.updateNewestNumber(it.data.num)
                            }
                        }
                    }
                    is XkcdResult.Error -> {
                        withContext(Dispatchers.Main) {
                            _uiState.value = uiState.value?.copy(isError = true)
                        }
                    }
                }
            }
        }
    }
    private fun applyResponseToState(data : XkcdResponse) {
        _uiState.postValue(
            MainUiModel(
                title = data.title,
                imageUrl = data.img,
                altText = data.alt.blankIfNull(),
                link = data.link.blankIfNull(),
                number = data.num,
                newestNumber = data.num.coerceAtLeast(_uiState.value?.newestNumber ?: 0),
                isError = false
            )
        )

    }
}