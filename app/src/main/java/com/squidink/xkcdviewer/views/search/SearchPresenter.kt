package com.squidink.xkcdviewer.views.search

import com.squidink.xkcdviewer.data.remoteobjects.GoogleSearchResponse
import com.squidink.xkcdviewer.data.remoteobjects.GoogleSearchResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchPresenter @Inject constructor(
    private var view: SearchContract.View?,
    private var model: SearchContract.Model?
) : SearchContract.Presenter {

    private var currentSearchResults : GoogleSearchResponse? = null
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
            when(result) {
                is GoogleSearchResult.Success -> {
                    currentSearchResults = result.data
                    updateView(result.data)
                }
                is GoogleSearchResult.Error -> {
                    view?.showErrorDialog(result.message)
                }
                else -> {
                    view?.showErrorDialog("Unknown error")
                }
            }
        }
    }

    private fun updateView(results : GoogleSearchResponse?) {
        results?.let {
            view?.showNextButton(it.queries?.nextPage != null)
            view?.showPrevButton(it.queries?.previousPage != null)
            view?.displaySearchResults(it)
        }

    }
}