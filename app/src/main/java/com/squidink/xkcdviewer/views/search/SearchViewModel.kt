package com.squidink.xkcdviewer.views.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squidink.xkcdviewer.data.remoteobjects.GoogleSearchItems
import com.squidink.xkcdviewer.data.remoteobjects.GoogleSearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private var model: SearchModel
) : ViewModel() {
    private val _uiState = MutableLiveData<SearchUiModel>(SearchUiModel.EMPTY)
    val uiState get() = _uiState as LiveData<SearchUiModel>

    fun submitSearchQuery(query: String) {
        submitSearchQuery(query, 1)
    }

    fun nextPage() {
        _uiState.value?.searchResult?.queries?.nextPage?.get(0)?.let {
            submitSearchQuery(it.searchTerms, it.startIndex)
        }
    }

    fun prevPage() {
        _uiState.value?.searchResult?.queries?.previousPage?.get(0)?.let {
            submitSearchQuery(it.searchTerms, it.startIndex)
        }
    }

    fun selectSearchResult(result : GoogleSearchItems) {
        _uiState.value = _uiState.value?.copy(selectedResult = result)
    }

    private fun submitSearchQuery(query : String, start : Int) {
        viewModelScope.launch {
            _uiState.value = uiState.value?.copy(isLoading = true)
            val result = withContext(Dispatchers.IO) {
                model.performSearch(query, start)
            }
            when(result) {
                is GoogleSearchResult.Success -> {
                    _uiState.value = _uiState.value?.copy(searchResult = result.data)
                }
                else -> {
                    _uiState.value = uiState.value?.copy(isError = true)
                }
            }
            _uiState.value = uiState.value?.copy(isLoading = false)
        }
    }
}