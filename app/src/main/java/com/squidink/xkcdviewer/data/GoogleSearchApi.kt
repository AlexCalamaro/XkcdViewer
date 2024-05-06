package com.squidink.xkcdviewer.data

import com.squidink.xkcdviewer.data.remoteobjects.GoogleSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleSearchApi {
    @GET("/customsearch/v1?")
    suspend fun getSearchResults(
        @Query("cx") searchInstance : String,
        @Query("q") query : String,
        @Query("key") apiKey : String,
        @Query("start") start : Int
    ): Response<GoogleSearchResponse>
}