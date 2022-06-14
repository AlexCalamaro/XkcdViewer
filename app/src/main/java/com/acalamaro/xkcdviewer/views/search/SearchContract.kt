package com.acalamaro.xkcdviewer.views.search

import com.acalamaro.xkcdviewer.data.remoteobjects.GoogleSearchBaseObject

interface SearchContract {

    interface View {
        fun displaySearchResults(results : GoogleSearchBaseObject)
        fun showPrevButton(visible : Boolean)
        fun showNextButton(visible : Boolean)
    }

    interface Presenter{
        fun onViewCreated()
        fun submitSearchQuery(query : String)
        fun onDestroy()
        fun nextPage()
        fun prevPage()
    }

    interface Model {
        suspend fun performSearch(query : String, start: Int) : GoogleSearchBaseObject
    }
}