package com.youssefelsa3ed.audiocontent.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.youssefelsa3ed.audiocontent.data.api.HomeApiService
import com.youssefelsa3ed.audiocontent.data.api.SearchApiService
import com.youssefelsa3ed.audiocontent.data.model.ContentItem
import com.youssefelsa3ed.audiocontent.data.model.ContentItemDeserializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val HOME_BASE_URL = "https://api-v2-b2sit6oh3a-uc.a.run.app/"
    private const val SEARCH_BASE_URL = "https://mock.apidog.com/m1/735111-711675-default/"

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @GsonConverter
    fun provideGsonConverter(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(ContentItem::class.java, ContentItemDeserializer())
            .create()
    }

    @Provides
    @Singleton
    @HomeApi
    fun provideHomeRetrofit(okHttpClient: OkHttpClient, @GsonConverter gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(HOME_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    @SearchApi
    fun provideSearchRetrofit(okHttpClient: OkHttpClient, @GsonConverter gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(SEARCH_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)) // Uses default Gson
            .build()
    }

    @Provides
    @Singleton
    fun provideHomeApiService(@HomeApi retrofit: Retrofit): HomeApiService {
        return retrofit.create(HomeApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchApiService(@SearchApi retrofit: Retrofit): SearchApiService {
        return retrofit.create(SearchApiService::class.java)
    }
}
