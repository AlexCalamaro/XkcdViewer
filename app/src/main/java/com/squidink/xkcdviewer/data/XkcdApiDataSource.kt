package com.squidink.xkcdviewer.data

import com.squidink.xkcdviewer.data.remoteobjects.XkcdResponse
import com.squidink.xkcdviewer.data.remoteobjects.XkcdResult
import javax.inject.Singleton

@Singleton
class XkcdApiDataSource (private val api : XkcdApi) {
    suspend fun getLatestXkcd(): XkcdResult<XkcdResponse> {
        return try {
            val result = api.getLatestXkcd()
            if(result.isSuccessful) {
                XkcdResult.Success(result.body()!!)
            } else {
                XkcdResult.Error(result.message())
            }
        } catch (e: Exception) {
            return XkcdResult.Error(e.message ?: "Unknown error")
        }
    }
    suspend fun getXkcdWithNumber(number : Int): XkcdResult<XkcdResponse> {

        return try {
            val result = api.getXkcdWithNumber(number)
            if(result.isSuccessful) {
                XkcdResult.Success(result.body()!!)
            } else {
                XkcdResult.Error(result.message())
            }
        } catch (e: Exception) {
            return XkcdResult.Error(e.message ?: "Unknown error")
        }
    }
}