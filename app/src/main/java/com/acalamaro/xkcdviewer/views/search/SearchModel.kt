package com.acalamaro.xkcdviewer.views.search

import com.acalamaro.xkcdviewer.data.GoogleSearchApiDataSource
import com.acalamaro.xkcdviewer.data.remoteobjects.GoogleSearchBaseObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchModel @Inject constructor(
    private val googleApiDataSource : GoogleSearchApiDataSource,
    private val apiKey : String,
    private val searchInstanceId : String
) : SearchContract.Model {

    override suspend fun performSearch(query: String, start: Int) : GoogleSearchBaseObject {
        return googleApiDataSource.getSearchResults(
            searchInstanceId,
            query,
            apiKey,
            start)
    }
}