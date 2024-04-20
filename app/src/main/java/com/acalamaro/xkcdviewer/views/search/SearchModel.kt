package com.acalamaro.xkcdviewer.views.search

import com.acalamaro.xkcdviewer.data.GoogleSearchApiDataSource
import com.acalamaro.xkcdviewer.data.remoteobjects.GoogleSearchResponse
import com.acalamaro.xkcdviewer.data.remoteobjects.GoogleSearchResult
import javax.inject.Inject

class SearchModel @Inject constructor(
    private val googleApiDataSource : GoogleSearchApiDataSource,
    private val apiKey : String,
    private val searchInstanceId : String
) : SearchContract.Model {

    override suspend fun performSearch(query: String, start: Int) : GoogleSearchResult<GoogleSearchResponse> {
        val result = googleApiDataSource.getSearchResults(
            searchInstanceId,
            query,
            apiKey,
            start)
        return when(result) {
            is GoogleSearchResult.Success -> {
                GoogleSearchResult.Success(result.data)
            }

            is GoogleSearchResult.Error -> {
                GoogleSearchResult.Error(result.message)
            }
        }
    }
}