package com.squidink.xkcdviewer.views.search

import com.squidink.xkcdviewer.data.remoteobjects.GoogleSearchItems
import com.squidink.xkcdviewer.data.remoteobjects.GoogleSearchResponse

data class SearchUiModel(
    val searchResult: GoogleSearchResponse?,
    val selectedResult: GoogleSearchItems?,
    val shouldShowNextButton: Boolean,
    val shouldShowPreviousButton: Boolean,
    val isLoading: Boolean,
    val isError: Boolean
) {
    companion object {
        val EMPTY = SearchUiModel(
            searchResult = null,
            null,
            shouldShowNextButton = false,
            shouldShowPreviousButton = false,
            isLoading = false,
            isError = false
        )
    }
}