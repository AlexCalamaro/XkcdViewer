package com.acalamaro.xkcdviewer.data.remoteobjects

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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