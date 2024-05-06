package com.squidink.xkcdviewer.views.search

import android.content.Context
import com.squidink.xkcdviewer.BuildConfig
import com.squidink.xkcdviewer.R
import com.squidink.xkcdviewer.data.GoogleSearchApiDataSource
import com.squidink.xkcdviewer.data.remoteobjects.GoogleSearchResponse
import com.squidink.xkcdviewer.data.remoteobjects.GoogleSearchResult
import javax.inject.Inject

class SearchModel @Inject constructor(
    private val googleApiDataSource : GoogleSearchApiDataSource,
    private val apiKey : String,
    private val searchInstanceId : String,
    private val context: Context
) : SearchContract.Model {

    override suspend fun performSearch(query: String, start: Int) : GoogleSearchResult<GoogleSearchResponse> {
        var packageName = context.getString(R.string.package_name_release)
        var signature = context.getString(R.string.sha_release)
        if(BuildConfig.DEBUG) {
            packageName = context.getString(R.string.package_name_debug)
            signature = context.getString(R.string.sha_debug)
        }

        val result = googleApiDataSource.getSearchResults(
            searchInstanceId,
            query,
            apiKey,
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