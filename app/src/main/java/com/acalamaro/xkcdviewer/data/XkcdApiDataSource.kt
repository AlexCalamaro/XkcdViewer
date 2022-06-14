package com.acalamaro.xkcdviewer.data

import javax.inject.Singleton

@Singleton
class XkcdApiDataSource (private val api : XkcdApi) {
    suspend fun getLatestXkcd() = api.getLatestXkcd()
    suspend fun getXkcdWithNumber(number : Int) = api.getXkcdWithNumber(number)
}