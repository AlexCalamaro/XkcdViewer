package com.acalamaro.xkcdviewer.views.search

import com.acalamaro.xkcdviewer.data.remoteobjects.GoogleSearchResponse
import com.acalamaro.xkcdviewer.data.remoteobjects.GoogleSearchResult

interface SearchContract {

    interface View {
        fun displaySearchResults(results : GoogleSearchResponse)
        fun showPrevButton(visible : Boolean)
        fun showNextButton(visible : Boolean)
        fun showErrorDialog(message : String)
    }

    interface Presenter{
        fun onViewCreated()
        fun submitSearchQuery(query : String)
        fun onDestroy()
        fun nextPage()
        fun prevPage()
    }

    interface Model {
        suspend fun performSearch(query : String, start: Int) : GoogleSearchResult<GoogleSearchResponse>
    }
}