package com.acalamaro.xkcdviewer.hilt

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.acalamaro.xkcdviewer.R
import com.acalamaro.xkcdviewer.data.GoogleSearchApiDataSource
import com.acalamaro.xkcdviewer.data.XkcdApiDataSource
import com.acalamaro.xkcdviewer.views.main.MainActivity
import com.acalamaro.xkcdviewer.views.main.MainContract
import com.acalamaro.xkcdviewer.views.main.MainFragment
import com.acalamaro.xkcdviewer.views.main.MainModel
import com.acalamaro.xkcdviewer.views.main.MainPresenter
import com.acalamaro.xkcdviewer.views.search.SearchContract
import com.acalamaro.xkcdviewer.views.search.SearchFragment
import com.acalamaro.xkcdviewer.views.search.SearchModel
import com.acalamaro.xkcdviewer.views.search.SearchPresenter
import com.squareup.picasso.Picasso
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object MainActivityModule {

    @Provides
    fun provideActivity(activity: Activity) : MainActivity {
        return activity as MainActivity
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
    fun provideMainPresenter(view : MainFragment, model : MainModel) : MainPresenter {
        return MainPresenter(view, model)
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
    @Singleton
    fun provideSearchPresenter(view : SearchFragment, model: SearchModel) : SearchPresenter {
        return SearchPresenter(view, model)
    }

    @Provides
    @Singleton
    fun provideSearchModel(googleApiDataSource: GoogleSearchApiDataSource, context: Context) : SearchModel {
        return SearchModel(googleApiDataSource, context.getString(R.string.google_api_key), context.getString(R.string.search_instance_identifier))
    }

    @Provides
    @Singleton
    fun provideSearchRecyclerLM(fragment : SearchFragment) : LinearLayoutManager {
        return LinearLayoutManager(fragment.context)
    }

    @Provides
    @Singleton
    fun providePicasso() : Picasso {
        return Picasso.get()
    }
}

@InstallIn(ActivityComponent::class)
@Module
abstract class SearchModule {
    @Binds
    abstract fun bindSearchFragment(fragment: SearchFragment): SearchContract.View

    @Binds
    abstract fun bindSearchPresenter(presenter : SearchPresenter) : SearchContract.Presenter

    @Binds
    abstract fun bindSearchModel(model : SearchModel) : SearchContract.Model
}


@InstallIn(ActivityComponent::class)
@Module
abstract class MainModule {
    @Binds
    abstract fun bindMainFragment(activity: MainFragment): MainContract.View

    @Binds
    abstract fun bindMainPresenter(presenter : MainPresenter) : MainContract.Presenter

    @Binds
    abstract fun bindMainModel(model : MainModel) : MainContract.Model
}