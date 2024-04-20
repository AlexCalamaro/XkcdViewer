package com.acalamaro.xkcdviewer.data

import com.acalamaro.xkcdviewer.data.remoteobjects.GoogleSearchResponse
import com.acalamaro.xkcdviewer.data.remoteobjects.GoogleSearchResult
import javax.inject.Singleton

@Singleton
class GoogleSearchApiDataSource (private val api : GoogleSearchApi) {
    suspend fun getSearchResults(
        searchInstance : String,
        query : String,
        apiKey : String,
        start : Int
    ): GoogleSearchResult<GoogleSearchResponse> {
        return try {
            val result = api.getSearchResults(searchInstance, query, apiKey, start)
            if(result.isSuccessful) {
                GoogleSearchResult.Success(result.body()!!)
            } else {
                GoogleSearchResult.Error(result.message())
            }
        } catch (e: Exception) {
            return GoogleSearchResult.Error(e.message ?: "Unknown error")
        }
    }
}