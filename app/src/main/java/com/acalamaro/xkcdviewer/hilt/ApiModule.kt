package com.acalamaro.xkcdviewer.hilt

import android.content.Context
import com.acalamaro.xkcdviewer.data.GoogleSearchApi
import com.acalamaro.xkcdviewer.data.GoogleSearchApiDataSource
import com.acalamaro.xkcdviewer.data.SettingsDataSource
import com.acalamaro.xkcdviewer.data.XkcdApi
import com.acalamaro.xkcdviewer.data.XkcdApiDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    private const val XKCD_BASE_URL = "https://www.xkcd.com"
    private const val GOOGLE_BASE_URL = "https://customsearch.googleapis.com"

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    @Named("xkcd")
    fun provideXkcdRetrofit(okHttpClient: OkHttpClient) : Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(XKCD_BASE_URL)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    @Named("google")
    fun provideGoogleRetrofit(okHttpClient: OkHttpClient) : Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(GOOGLE_BASE_URL)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideXkcdApi(@Named("xkcd") retrofit: Retrofit) : XkcdApi = retrofit.create(XkcdApi::class.java)

    @Singleton
    @Provides
    fun provideXkcdApiDataSource(api : XkcdApi) : XkcdApiDataSource = XkcdApiDataSource(api)

    @Singleton
    @Provides
    fun provideGoogleApi(@Named("google") retrofit: Retrofit) : GoogleSearchApi = retrofit.create(GoogleSearchApi::class.java)

    @Singleton
    @Provides
    fun provideGoogleApiDataSource(api : GoogleSearchApi) : GoogleSearchApiDataSource = GoogleSearchApiDataSource(api)

    @Singleton
    @Provides
    fun provideSettingsDataSource(@ApplicationContext context: Context) : SettingsDataSource = SettingsDataSource(context)

}