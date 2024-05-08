package com.squidink.xkcdviewer.views.main

data class MainUiModel (
    val title: String,
    val imageUrl: String,
    val altText: String,
    val link: String,
    val number: Int?,
    val newestNumber: Int?,
    val isError: Boolean,
    val isLoading: Boolean
) { companion object {
        val EMPTY = MainUiModel(
            title = "",
            imageUrl = "",
            altText = "",
            link = "",
            number = null,
            newestNumber = null,
            isError = false,
            isLoading = false
        )
    }
}