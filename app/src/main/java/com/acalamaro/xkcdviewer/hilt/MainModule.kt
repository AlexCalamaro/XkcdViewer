package com.acalamaro.xkcdviewer.hilt

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.acalamaro.xkcdviewer.R
import com.acalamaro.xkcdviewer.data.GoogleSearchApiDataSource
import com.acalamaro.xkcdviewer.data.SettingsDataSource
import com.acalamaro.xkcdviewer.data.XkcdApiDataSource
import com.acalamaro.xkcdviewer.views.main.MainActivity
import com.acalamaro.xkcdviewer.views.main.MainFragment
import com.acalamaro.xkcdviewer.views.main.MainModel
import com.acalamaro.xkcdviewer.views.search.SearchFragment
import com.acalamaro.xkcdviewer.views.search.SearchModel
import com.acalamaro.xkcdviewer.views.search.SearchPresenter
import com.acalamaro.xkcdviewer.views.settings.SettingsFragment
import com.acalamaro.xkcdviewer.views.settings.SettingsModel
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object MainActivityModule {

    @Provides
    fun provideActivity(activity: Activity) : MainActivity {
        return activity as MainActivity
    }

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }
}

@InstallIn(SingletonComponent::class)
@Module
class FragmentsModule {
    @Provides
    fun provideMainFragment() : MainFragment {
        return MainFragment()
    }

    @Provides
    @Singleton
    fun provideMainModel(xkcdApiDataSource: XkcdApiDataSource) : MainModel {
        return MainModel(xkcdApiDataSource)
    }

    @Provides
    fun provideSearchFragment(): SearchFragment {
        return SearchFragment()
    }

    @Provides
    fun provideSearchPresenter(view : SearchFragment, model: SearchModel) : SearchPresenter {
        return SearchPresenter(view, model)
    }

    @Provides
    @Singleton
    fun provideSearchModel(googleApiDataSource: GoogleSearchApiDataSource, context: Context) : SearchModel {
        return SearchModel(googleApiDataSource, context.getString(R.string.google_api_key), context.getString(R.string.search_instance_identifier))
    }

    @Provides
    fun provideSearchRecyclerLM(fragment : SearchFragment) : LinearLayoutManager {
        return LinearLayoutManager(fragment.context)
    }

    @Provides
    fun provideSettingsFragment(): SettingsFragment {
        return SettingsFragment()
    }

    @Provides
    @Singleton
    fun provideSettingsModel(dataSource: SettingsDataSource) : SettingsModel {
        return SettingsModel(dataSource)
    }

    @Provides
    @Singleton
    fun providePicasso() : Picasso {
        return Picasso.get()
    }
}