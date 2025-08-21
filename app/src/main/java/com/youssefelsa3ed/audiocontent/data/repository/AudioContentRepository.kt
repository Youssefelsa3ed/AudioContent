package com.youssefelsa3ed.audiocontent.data.repository

import com.youssefelsa3ed.audiocontent.data.api.HomeApiService
import com.youssefelsa3ed.audiocontent.data.api.SearchApiService
import com.youssefelsa3ed.audiocontent.data.model.SearchResponse
import com.youssefelsa3ed.audiocontent.data.model.HomeSectionsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioContentRepository @Inject constructor(
    private val homeApiService: HomeApiService,
    private val searchApiService: SearchApiService
) {

    fun getHomeSections(page: Int = 1): Flow<Resource<HomeSectionsResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = homeApiService.getHomeSections(page)
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Failed to load home sections: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error occurred"))
        }
    }

    fun searchSections(query: String): Flow<Resource<SearchResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = searchApiService.searchSections(query)
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Search failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Search error occurred"))
        }
    }
}

sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val message: String) : Resource<T>()
    class Loading<T> : Resource<T>()
}