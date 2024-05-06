package com.squidink.xkcdviewer.hilt

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.squidink.xkcdviewer.R
import com.squidink.xkcdviewer.data.GoogleSearchApiDataSource
import com.squidink.xkcdviewer.data.SettingsDataSource
import com.squidink.xkcdviewer.data.XkcdApiDataSource
import com.squidink.xkcdviewer.views.main.MainActivity
import com.squidink.xkcdviewer.views.main.MainFragment
import com.squidink.xkcdviewer.views.main.MainModel
import com.squidink.xkcdviewer.views.search.SearchFragment
import com.squidink.xkcdviewer.views.search.SearchModel
import com.squidink.xkcdviewer.views.search.SearchPresenter
import com.squidink.xkcdviewer.views.settings.SettingsFragment
import com.squidink.xkcdviewer.views.settings.SettingsModel
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
    fun provideMainModel(xkcdApiDataSource: XkcdApiDataSource, settingsDataSource: SettingsDataSource) : MainModel {
        return MainModel(xkcdApiDataSource, settingsDataSource)
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