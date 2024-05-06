package com.squidink.xkcdviewer.extensions

internal fun String?.blankIfNull(): String {
    return this ?: ""
}