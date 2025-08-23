package com.youssefelsa3ed.audiocontent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.youssefelsa3ed.audiocontent.data.model.Pagination
import com.youssefelsa3ed.audiocontent.data.model.Section
import com.youssefelsa3ed.audiocontent.data.repository.AudioContentRepository
import com.youssefelsa3ed.audiocontent.data.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.net.toUri
import com.youssefelsa3ed.audiocontent.data.model.ContentItem

data class HomeUiState(
    val sections: List<Section> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val errorMessage: String? = null,
    val pagination: Pagination? = null,
    val currentPage: Int = 1,
    val canLoadMore: Boolean = false,
    val playingUri: String? = null,
    val playingContent: ContentItem? = null,
    val playingPosition: Long = 0,
    val duration: Long = 0,
    val isPlaying: Boolean = false,
    val loadingAudioContent: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AudioContentRepository,
    private val exoPlayer: ExoPlayer
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var progressJob: Job? = null

    fun playAudio(content: ContentItem, url: String) {
        val mediaItem = MediaItem.fromUri(url.toUri())
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()

        _uiState.value = _uiState.value.copy(
            playingUri = url,
            playingContent = content,
            playingPosition = 0,
            isPlaying = true,
            loadingAudioContent = true
        )
        exoPlayer.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                _uiState.value = _uiState.value.copy(duration = exoPlayer.duration, loadingAudioContent = false)
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    _uiState.value = _uiState.value.copy(duration = exoPlayer.duration, loadingAudioContent = false)
                }
            }
        })

        startProgressUpdates()
    }

    private fun startProgressUpdates() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (true) {
                _uiState.value = _uiState.value.copy(
                    playingPosition = exoPlayer.currentPosition
                )
                delay(250L)
            }
        }
    }

    fun pauseAudio() {
        exoPlayer.pause()
        _uiState.value = _uiState.value.copy(isPlaying = false)
    }

    fun closeAudio() {
        exoPlayer.stop()
        _uiState.value = _uiState.value.copy(isPlaying = false, playingContent = null, playingUri = null)
    }

    fun resumeAudio() {
        exoPlayer.play()
        _uiState.value = _uiState.value.copy(isPlaying = true)
    }

    override fun onCleared() {
        super.onCleared()
        progressJob?.cancel()
        exoPlayer.release()
    }

    init {
        loadHomeSections()
    }

    fun loadHomeSections(page: Int = 1) {
        viewModelScope.launch {
            repository.getHomeSections(page).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.value = if (page == 1)
                            _uiState.value.copy(
                                isLoading = true,
                                errorMessage = null
                            )
                        else
                            _uiState.value.copy(
                                isLoadingMore = true,
                                errorMessage = null
                            )
                    }
                    is Resource.Success -> {
                        val newSections = if (page == 1)
                            resource.data.sections.sortedBy { it.order }
                        else
                            _uiState.value.sections + resource.data.sections.sortedBy { it.order }

                        _uiState.value = _uiState.value.copy(
                            sections = newSections,
                            isLoading = false,
                            isLoadingMore = false,
                            errorMessage = null,
                            pagination = resource.data.pagination,
                            currentPage = page,
                            canLoadMore = resource.data.pagination.nextPage != null
                        )
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            errorMessage = resource.message
                        )
                    }
                }
            }
        }
    }

    fun loadMoreSections() {
        if (_uiState.value.canLoadMore && !_uiState.value.isLoadingMore) {
            loadHomeSections(_uiState.value.currentPage + 1)
        }
    }

    fun retry() {
        loadHomeSections()
    }
}