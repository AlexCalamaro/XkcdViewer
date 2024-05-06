package com.squidink.xkcdviewer.views.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.squidink.xkcdviewer.data.remoteobjects.GoogleSearchItems

class SearchResultViewModel : ViewModel() {

    val searchResult = MutableLiveData<GoogleSearchItems>()

    fun postSearchResult(result : GoogleSearchItems) {
        searchResult.value = result
    }
}