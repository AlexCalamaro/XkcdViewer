package com.acalamaro.xkcdviewer.views.search

import android.os.Handler
import android.os.Looper
import com.acalamaro.xkcdviewer.data.remoteobjects.GoogleSearchBaseObject
import com.acalamaro.xkcdviewer.views.main.MainFragment
import com.acalamaro.xkcdviewer.views.main.MainPresenter
import kotlinx.coroutines.*
import javax.inject.Inject

class SearchPresenter @Inject constructor(
    private var view: SearchContract.View?,
    private var model: SearchContract.Model?
) : SearchContract.Presenter {

    var currentSearchResults : GoogleSearchBaseObject? = null
    private var job : Job? = null

    override fun onViewCreated() {
        currentSearchResults?.let {
            updateView(it)
        }
    }

    override fun submitSearchQuery(query: String) {
        submitSearchQuery(query, 1)
    }

    override fun onDestroy() {
        job?.cancel(null)
        view = null
        model = null
    }

    override fun nextPage() {
        currentSearchResults?.queries?.nextPage?.get(0)?.let {
            submitSearchQuery(it.searchTerms, it.startIndex)
        }
    }

    override fun prevPage() {
        currentSearchResults?.queries?.previousPage?.get(0)?.let {
            submitSearchQuery(it.searchTerms, it.startIndex)
        }
    }

    private fun submitSearchQuery(query : String, start : Int) {
        job = CoroutineScope(Dispatchers.Main).launch {
            val result = withContext(Dispatchers.IO) {
                model?.performSearch(query, start)
            }
            currentSearchResults = result
            updateView(result)
        }
    }

    private fun updateView(results : GoogleSearchBaseObject?) {
        results?.let {
            view?.showNextButton(it.queries?.nextPage != null)
            view?.showPrevButton(it.queries?.previousPage != null)
            view?.displaySearchResults(it)
        }

    }
}