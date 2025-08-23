package com.youssefelsa3ed.audiocontent

import androidx.media3.exoplayer.ExoPlayer
import com.youssefelsa3ed.audiocontent.data.model.HomeSectionsResponse
import com.youssefelsa3ed.audiocontent.data.model.Pagination
import com.youssefelsa3ed.audiocontent.data.model.PodcastContent
import com.youssefelsa3ed.audiocontent.data.model.Section
import com.youssefelsa3ed.audiocontent.data.repository.AudioContentRepository
import com.youssefelsa3ed.audiocontent.data.repository.Resource
import com.youssefelsa3ed.audiocontent.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
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
class HomeViewModelTest {

    @Mock
    private lateinit var repository: AudioContentRepository

    @Mock
    private lateinit var exoPlayer: ExoPlayer

    private lateinit var viewModel: HomeViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is loading`() = runTest {
        whenever(repository.getHomeSections(1)).thenReturn(flowOf(Resource.Loading()))

        viewModel = HomeViewModel(repository, exoPlayer)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isLoading)
        assertTrue(viewModel.uiState.value.sections.isEmpty())
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `successful data load updates UI state correctly`() = runTest {
        val mockContent = listOf(
            PodcastContent(
                "1",
                "Test Podcast",
                "Description",
                "image.jpg",
                10,
                3600,
                "en",
                "5",
                "9",
                "100.0"
            )
        )
        val mockSections = listOf(
            Section("Podcasts", "square", "podcast", "1", mockContent),
            Section("Episodes", "2_lines_grid", "episode", "2", emptyList())
        )
        val mockPagination = Pagination("/home_sections?page=2", 10)
        val mockResponse = HomeSectionsResponse(mockSections, mockPagination)

        whenever(repository.getHomeSections(1)).thenReturn(
            flowOf(Resource.Loading(), Resource.Success(mockResponse))
        )

        viewModel = HomeViewModel(repository, exoPlayer)
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(2, viewModel.uiState.value.sections.size)
        assertTrue(viewModel.uiState.value.canLoadMore)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `error response updates UI state with error message`() = runTest {
        val errorMessage = "Network error"
        whenever(repository.getHomeSections(1)).thenReturn(
            flowOf(Resource.Loading(), Resource.Error(errorMessage))
        )

        viewModel = HomeViewModel(repository, exoPlayer)
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertTrue(viewModel.uiState.value.sections.isEmpty())
        assertEquals(errorMessage, viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `loadMoreSections appends new sections to existing ones`() = runTest {
        val firstPageContent = listOf(
            PodcastContent("1", "Test Podcast 1", "Description", "image.jpg", 10, 3600, "en", "5", "9", "100.0")
        )
        val firstPageSections = listOf(
            Section("Podcasts", "square", "podcast", "1", firstPageContent)
        )
        val firstPagePagination = Pagination("/home_sections?page=2", 10)
        val firstPageResponse = HomeSectionsResponse(firstPageSections, firstPagePagination)

        val secondPageContent = listOf(
            PodcastContent("2", "Test Podcast 2", "Description", "image.jpg", 10, 3600, "en", "5", "9", "100.0")
        )
        val secondPageSections = listOf(
            Section("More Podcasts", "square", "podcast", "2", secondPageContent)
        )
        val secondPagePagination = Pagination(null, 10)
        val secondPageResponse = HomeSectionsResponse(secondPageSections, secondPagePagination)

        whenever(repository.getHomeSections(1)).thenReturn(
            flowOf(Resource.Loading(), Resource.Success(firstPageResponse))
        )
        whenever(repository.getHomeSections(2)).thenReturn(
            flowOf(Resource.Loading(), Resource.Success(secondPageResponse))
        )

        viewModel = HomeViewModel(repository, exoPlayer)
        advanceUntilIdle()

        viewModel.loadMoreSections()
        advanceUntilIdle()

        assertEquals(2, viewModel.uiState.value.sections.size)
        assertFalse(viewModel.uiState.value.canLoadMore)
        assertEquals(2, viewModel.uiState.value.currentPage)
    }
}