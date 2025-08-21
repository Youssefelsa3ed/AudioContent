package com.youssefelsa3ed.audiocontent.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HomeApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SearchApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GsonConverter
