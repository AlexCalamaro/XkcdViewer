package com.squidink.xkcdviewer.hilt

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.squidink.xkcdviewer.data.GoogleSearchApiDataSource
import com.squidink.xkcdviewer.data.SettingsDataSource
import com.squidink.xkcdviewer.data.XkcdApiDataSource
import com.squidink.xkcdviewer.views.main.MainActivity
import com.squidink.xkcdviewer.views.main.MainFragment
import com.squidink.xkcdviewer.views.main.MainModel
import com.squidink.xkcdviewer.views.search.SearchFragment
import com.squidink.xkcdviewer.views.search.SearchModel
import com.squidink.xkcdviewer.views.settings.SettingsFragment
import com.squidink.xkcdviewer.views.settings.SettingsModel
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


    @Provides
    fun provideSearchFragment(): SearchFragment {
        return SearchFragment()
    }

    @Provides
    fun provideSettingsFragment(): SettingsFragment {
        return SettingsFragment()
    }

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
    @Singleton
    fun provideSearchModel(googleApiDataSource: GoogleSearchApiDataSource) : SearchModel {
        return SearchModel(googleApiDataSource)
    }

    @Provides
    fun provideSearchRecyclerLM(fragment : SearchFragment) : LinearLayoutManager {
        return LinearLayoutManager(fragment.context)
    }

    @Provides
    @Singleton
    fun provideSettingsModel(dataSource: SettingsDataSource) : SettingsModel {
        return SettingsModel(dataSource)
    }
}

@InstallIn(SingletonComponent::class)
@Module
class PicassoModule {
    @Provides
    @Singleton
    fun providePicasso(@ApplicationContext context: Context) : Picasso {
        return Picasso.Builder(context).build()
    }
}