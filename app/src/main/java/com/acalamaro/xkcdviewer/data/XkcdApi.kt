package com.acalamaro.xkcdviewer.data

import com.acalamaro.xkcdviewer.data.remoteobjects.XkcdResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface XkcdApi {
    @GET("/{number}/info.0.json")
    suspend fun getXkcdWithNumber(@Path("number") number : Int) : Response<XkcdResponse>

    @GET("/info.0.json")
    suspend fun getLatestXkcd() : Response<XkcdResponse>
}