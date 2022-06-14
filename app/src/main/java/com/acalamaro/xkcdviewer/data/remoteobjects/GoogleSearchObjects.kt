package com.acalamaro.xkcdviewer.data.remoteobjects

import com.google.gson.annotations.SerializedName

data class GoogleSearchBaseObject(
    val kind: String?,
    val searchInformation: GoogleSearchInformation?,
    val items: List<GoogleSearchItems>?,
    val queries: SearchQueries?
)

data class GoogleSearchInformation(
    val totalResults : Int
)

data class GoogleSearchItems(
    val title: String,
    val link: String,
    val snippet: String,
    @SerializedName("pagemap")
    val pageMap: PageMap?
)

data class PageMap(
    @SerializedName("cse_thumbnail")
    val cseThumbnail: List<CSEThumbnail>
)

data class CSEThumbnail(
    val src: String
)

data class SearchQueries(
    val previousPage: List<SearchPageInformation>?,
    val request: List<SearchPageInformation>,
    val nextPage: List<SearchPageInformation>,
)

data class SearchPageInformation(
    val count: Int,
    val startIndex: Int,
    val totalResults: Int,
    val searchTerms: String
)
