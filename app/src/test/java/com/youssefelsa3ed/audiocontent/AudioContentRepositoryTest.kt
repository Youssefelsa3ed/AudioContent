package com.youssefelsa3ed.audiocontent

import com.youssefelsa3ed.audiocontent.data.api.HomeApiService
import com.youssefelsa3ed.audiocontent.data.api.SearchApiService
import com.youssefelsa3ed.audiocontent.data.model.HomeSectionsResponse
import com.youssefelsa3ed.audiocontent.data.model.Pagination
import com.youssefelsa3ed.audiocontent.data.model.PodcastContent
import com.youssefelsa3ed.audiocontent.data.model.SearchResponse
import com.youssefelsa3ed.audiocontent.data.model.Section
import com.youssefelsa3ed.audiocontent.data.repository.AudioContentRepository
import com.youssefelsa3ed.audiocontent.data.repository.Resource
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import retrofit2.Response

class AudioContentRepositoryTest {

    @Mock
    private lateinit var homeApiService: HomeApiService

    @Mock
    private lateinit var searchApiService: SearchApiService

    private lateinit var repository: AudioContentRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = AudioContentRepository(homeApiService, searchApiService)
    }

    @Test
    fun `getHomeSections returns success when API call is successful`() = runTest {
        val mockContent = listOf(
            PodcastContent("1", "Test Podcast", "Description", "image.jpg", 10, 3600, "en", "5", "9", "100.0")
        )
        val mockSections = listOf(
            Section("Podcasts", "square", "podcast", "1", mockContent)
        )
        val mockPagination = Pagination("/home_sections?page=2", 10)
        val mockResponse = HomeSectionsResponse(mockSections, mockPagination)
        whenever(homeApiService.getHomeSections(1)).thenReturn(Response.success(mockResponse))

        val result = repository.getHomeSections(1).toList()

        assertEquals(2, result.size)
        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Success)
        assertEquals(mockSections, (result[1] as Resource.Success).data.sections)
    }

    @Test
    fun `getHomeSections returns error when API call fails`() = runTest {
        whenever(homeApiService.getHomeSections(1)).thenReturn(Response.error(404, ResponseBody.EMPTY))

        val result = repository.getHomeSections(1).toList()

        assertEquals(2, result.size)
        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Error)
    }

    @Test
    fun `searchSections returns success when API call is successful`() = runTest {
        val mockItems = listOf(
            PodcastContent("1", "Test Podcast", "Description", "image.jpg", 10, 3600, "en", "5", "9", "100.0")
        )
        val searchResponse = listOf(
            Section("Podcasts", "square", "podcast", "1", mockItems)
        )
        val mockResponse = SearchResponse(searchResponse)
        whenever(searchApiService.searchSections()).thenReturn(Response.success(mockResponse))

        val result = repository.searchSections().toList()

        assertEquals(2, result.size)
        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Success)
        assertEquals(mockItems, (result[1] as Resource.Success).data.results.last().content)
    }
}