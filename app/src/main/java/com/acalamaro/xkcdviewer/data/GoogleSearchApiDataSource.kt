package com.acalamaro.xkcdviewer.data

import javax.inject.Singleton

@Singleton
class GoogleSearchApiDataSource (private val api : GoogleSearchApi) {
    suspend fun getSearchResults(
        searchInstance : String,
        query : String,
        apiKey : String,
        start : Int
    ) = api.getSearchResults(searchInstance, query, apiKey, start)
}