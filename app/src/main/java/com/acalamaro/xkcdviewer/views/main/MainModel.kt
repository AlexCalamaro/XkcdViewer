package com.acalamaro.xkcdviewer.views.main

import com.acalamaro.xkcdviewer.data.XkcdApiDataSource
import com.acalamaro.xkcdviewer.data.remoteobjects.XkcdResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainModel @Inject constructor(
    private val xkcdApiDataSource: XkcdApiDataSource
) : MainContract.Model {

    override suspend fun loadXkcd(number : Int?) : XkcdResponse?{
        return if(number == null) {
            xkcdApiDataSource.getLatestXkcd().body()
        } else {
            xkcdApiDataSource.getXkcdWithNumber(number).body()
        }
    }
}