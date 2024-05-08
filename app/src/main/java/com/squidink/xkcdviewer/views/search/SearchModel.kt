package com.squidink.xkcdviewer.views.search

import com.squidink.xkcdviewer.BuildConfig
import com.squidink.xkcdviewer.Secrets
import com.squidink.xkcdviewer.data.GoogleSearchApiDataSource
import com.squidink.xkcdviewer.data.remoteobjects.GoogleSearchResponse
import com.squidink.xkcdviewer.data.remoteobjects.GoogleSearchResult
import javax.inject.Inject

class SearchModel @Inject constructor(
    private val googleApiDataSource : GoogleSearchApiDataSource
) {

    suspend fun performSearch(query: String, start: Int) : GoogleSearchResult<GoogleSearchResponse> {
        var packageName = SearchConstants.PACKAGE_NAME_RELEASE
        var signature = SearchConstants.SHA_RELEASE
        if(BuildConfig.DEBUG) {
            packageName = SearchConstants.PACKAGE_NAME_DEBUG
            signature = SearchConstants.SHA_DEBUG
        }

        val result = googleApiDataSource.getSearchResults(
            Secrets.SEARCH_INSTANCE_IDENTIFIER,
            query,
            Secrets.GOOGLE_API_KEY,
            start,
            packageName,
            signature
        )

        return when(result) {
            is GoogleSearchResult.Success -> {
                // filter out links that don't have a url that looks like https://xkcd.com/###/
                val filteredItems = result.data.items?.filter {
                    it.link.matches(Regex("https://xkcd.com/\\d+/"))
                }
                GoogleSearchResult.Success(result.data.copy(items = filteredItems))
            }

            is GoogleSearchResult.Error -> {
                GoogleSearchResult.Error(result.message)
            }
        }
    }
}