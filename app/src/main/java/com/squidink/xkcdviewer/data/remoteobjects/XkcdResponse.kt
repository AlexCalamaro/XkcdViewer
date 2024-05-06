package com.squidink.xkcdviewer.data.remoteobjects

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

sealed class XkcdResult<out T> {
    data class Success<out T>(val data: XkcdResponse) : XkcdResult<T>()
    data class Error(val error: String) : XkcdResult<Nothing>()
}

@Parcelize
data class XkcdResponse (
    val month : Int,
    val num : Int,
    val link : String?,
    val year : Int,
    val news : String?,
    @SerializedName("safe_title")
    val safeTitle : String?,
    val transcript : String?,
    val alt: String?,
    val img : String,
    val title : String,
    val day : Int
    ) : Parcelable