package com.youssefelsa3ed.audiocontent.data.api

import com.youssefelsa3ed.audiocontent.data.model.HomeSectionsResponse
import com.youssefelsa3ed.audiocontent.data.model.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeApiService {
    @GET("home_sections")
    suspend fun getHomeSections(@Query("page") page: Int = 1): Response<HomeSectionsResponse>
}

interface SearchApiService {
    @GET("search")
    suspend fun searchSections(@Query("q") query: String): Response<SearchResponse>
}