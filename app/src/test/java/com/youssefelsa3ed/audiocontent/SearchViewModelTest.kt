package com.youssefelsa3ed.audiocontent

import androidx.media3.exoplayer.ExoPlayer
import com.youssefelsa3ed.audiocontent.data.model.PodcastContent
import com.youssefelsa3ed.audiocontent.data.model.SearchResponse
import com.youssefelsa3ed.audiocontent.data.model.Section
import com.youssefelsa3ed.audiocontent.data.repository.AudioContentRepository
import com.youssefelsa3ed.audiocontent.data.repository.Resource
import com.youssefelsa3ed.audiocontent.viewmodel.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @Mock
    private lateinit var repository: AudioContentRepository
    @Mock
    private lateinit var exoPlayer: ExoPlayer

    private lateinit var viewModel: SearchViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = SearchViewModel(repository, exoPlayer)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is empty`() {
        assertTrue(viewModel.uiState.value.query.isEmpty())
        assertTrue(viewModel.uiState.value.results.isEmpty())
        assertFalse(viewModel.uiState.value.isLoading)
        assertFalse(viewModel.uiState.value.hasSearched)
    }

    @Test
    fun `updateSearchQuery updates query in state`() {
        viewModel.updateSearchQuery("test query")

        assertEquals("test query", viewModel.uiState.value.query)
    }

    @Test
    fun `search with debounce delay works correctly`() = runTest {
        val mockItems = listOf(
            PodcastContent("1", "Test", null, null, 10, null, null, null, null, "100.0")
        )
        val searchResponse = listOf(
            Section("Podcasts", "square", "podcast", "1", mockItems)
        )
        val mockResponse = SearchResponse(searchResponse)
        whenever(repository.searchSections()).thenReturn(
            flowOf(Resource.Success(mockResponse))
        )

        viewModel.initSearch()
        viewModel.updateSearchQuery("test")

        advanceTimeBy(100)

        assertFalse(viewModel.uiState.value.hasSearched)

        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.hasSearched)
        assertEquals(1, viewModel.uiState.value.results.size)
    }

    @Test
    fun `empty query clears results`() {
        viewModel.updateSearchQuery("test")

        viewModel.updateSearchQuery("")

        assertTrue(viewModel.uiState.value.query.isEmpty())
        assertTrue(viewModel.uiState.value.results.isEmpty())
        assertFalse(viewModel.uiState.value.hasSearched)
    }
}