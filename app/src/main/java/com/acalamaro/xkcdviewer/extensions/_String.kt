package com.acalamaro.xkcdviewer.extensions

internal fun String?.blankIfNull(): String {
    return this ?: ""
}