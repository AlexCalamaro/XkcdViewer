package com.acalamaro.xkcdviewer.views.main

import android.os.Bundle
import com.acalamaro.xkcdviewer.data.remoteobjects.XkcdResponse
import retrofit2.Response

interface MainContract {
    interface View {
        // Set title of XKCD
        fun setTitle(title: String)

        // Set the comic number
        fun setNumber(number: String)

        // Set image
        fun setImage(image: String)

        // Set alt text and content description
        fun setAltText(altText: String)

        fun showToast(toastText : String)
    }

    interface Presenter {
        // Load latest XKCD
        fun loadLatest()
        // Load next XKCD
        fun loadNext()
        // Load previous XKCD
        fun loadPrevious()
        // Load Random XKCD
        fun loadRandom()
        // Load specific XKCD by number
        fun loadSpecific(selected: Int)

        // Get bundle for persisting instance
        fun requestInstanceBundle(bundle: Bundle)

        // Receive bundle for restoring instance
        fun onCreate(state: Bundle?)

        // Teardown
        fun onDestroy()
    }

    interface Model {
        suspend fun loadXkcd(number: Int?) : XkcdResponse?
    }
}