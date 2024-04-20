package com.acalamaro.xkcdviewer.views.main

import android.os.Bundle
import com.acalamaro.xkcdviewer.data.remoteobjects.XkcdResponse
import com.acalamaro.xkcdviewer.data.remoteobjects.XkcdResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainPresenter @Inject constructor(
    private var view: MainContract.View?,
    private var model: MainContract.Model?
) : MainContract.Presenter {

    companion object {
        const val bundle_tag_newest = "newest"
        const val bundle_tag_current = "current"
    }

    private var currentXkcd : XkcdResponse? = null
    private var newestXkcd : XkcdResponse? = null

    private var job : Job? = null

    override fun loadLatest() {
        loadXkcd(null)
    }

    override fun loadNext() {
        when(currentXkcd?.num) {
            null -> loadXkcd(null)
            newestXkcd?.num -> loadXkcd(1)
            else -> loadXkcd(currentXkcd!!.num+1)
        }
    }

    override fun loadPrevious() {
        when(currentXkcd?.num) {
            null, 1 -> loadXkcd(null)
            else -> loadXkcd(currentXkcd!!.num-1)
        }
    }

    override fun loadRandom() {
        when(newestXkcd?.num) {
            null -> loadXkcd((1..2631).random()) // Hardcoded current newest
            else -> loadXkcd((1..newestXkcd!!.num).random())
        }
    }

    override fun loadSpecific(selected: Int) {
        // If newest is null, state is weird and we don't know the range of valid values
        //  so just reload the newest again even if that might confuse the user
        if(newestXkcd?.num == null) {
            loadXkcd(null)
        } else if (selected in (1..newestXkcd!!.num)) {
            loadXkcd(selected)
        } else {
            view?.showToast("Invalid selection")
        }
    }

    // Puts instance data into the bundle provided by View
    override fun requestInstanceBundle(bundle: Bundle) {
        bundle.putParcelable(bundle_tag_current, currentXkcd)
        bundle.putParcelable(bundle_tag_newest, newestXkcd)
    }

    // Restores instance data to UI
    override fun onCreate(state: Bundle?) {
        state?.let {
            currentXkcd = state.getParcelable(bundle_tag_current)
            newestXkcd = state.getParcelable(bundle_tag_newest)
        }
        if(currentXkcd != null && newestXkcd != null) {
            applyResponseToView(currentXkcd!!)
        } else {
            loadLatest()
        }
    }

    override fun onDestroy() {
        job?.cancel(null)
        view = null
        model = null
    }

    private fun updateStateVariables(data : XkcdResponse) {
        currentXkcd = data
        if(newestXkcd == null || newestXkcd!!.num < currentXkcd!!.num) {
            newestXkcd = currentXkcd
        }
    }

    private fun loadXkcd(number : Int?) {
        job = CoroutineScope(Dispatchers.IO).launch {
            model?.loadXkcd(number)?.let {
                when(it) {
                    is XkcdResult.Success -> {
                        withContext(Dispatchers.Main) {
                            updateStateVariables(it.data)
                            applyResponseToView(it.data)
                        }
                    }
                    is XkcdResult.Error -> {
                        withContext(Dispatchers.Main) {
                            view?.showErrorDialog(it.error)
                        }
                    }
                }
            }
        }
    }

    private fun applyResponseToView(data : XkcdResponse) {
        view?.setTitle(data.title)
        view?.setNumber("#"+data.num.toString())
        data.alt?.let { view?.setAltText(it) }
        view?.setImage(data.img)
    }
}