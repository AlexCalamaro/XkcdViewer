package com.acalamaro.xkcdviewer.views.main

import com.acalamaro.xkcdviewer.data.XkcdApiDataSource
import com.acalamaro.xkcdviewer.data.remoteobjects.XkcdResponse
import com.acalamaro.xkcdviewer.data.remoteobjects.XkcdResult
import javax.inject.Inject

class MainModel @Inject constructor(
    private val xkcdApiDataSource: XkcdApiDataSource
) : MainContract.Model {

    override suspend fun loadXkcd(number : Int?) : XkcdResult<XkcdResponse> {
        return if(number == null) {
            xkcdApiDataSource.getLatestXkcd()
        } else {
            xkcdApiDataSource.getXkcdWithNumber(number)
        }

    }
}