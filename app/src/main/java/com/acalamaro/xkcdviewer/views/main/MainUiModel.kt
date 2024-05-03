package com.acalamaro.xkcdviewer.views.main

data class MainUiModel (
    val title: String,
    val imageUrl: String,
    val altText: String,
    val number: Int?,
    val newestNumber: Int?,
    val isError: Boolean
) { companion object {
        val EMPTY = MainUiModel(
            title = "",
            imageUrl = "",
            altText = "",
            number = null,
            newestNumber = null,
            isError = false
        )
    }
}