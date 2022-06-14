package com.acalamaro.xkcdviewer.utils

import java.util.regex.Pattern

class SearchParseUtils {
    companion object {
        fun getComicNumberFromUrl(url : String) : Int? {
            return if(isUrlValidXkcd(url)) {
                val stringResult = url.filter { it.isDigit() }
                return stringResult.toInt()
            } else{
                null
            }
        }

        fun isUrlValidXkcd(url : String) : Boolean {
            return Pattern.matches("https://xkcd.com/\\d*/", url)
        }
    }
}