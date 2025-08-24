package com.youssefelsa3ed.audiocontent.viewmodel

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.youssefelsa3ed.audiocontent.data.model.ContentItem
import com.youssefelsa3ed.audiocontent.data.repository.AudioContentRepository
import com.youssefelsa3ed.audiocontent.data.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: AudioContentRepository,
    private val exoPlayer: ExoPlayer
) : ViewModel() {

    private var progressJob: Job? = null

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _exoPlayerState = MutableStateFlow(ExoPlayerState())
    val exoPlayerState: StateFlow<ExoPlayerState> = _exoPlayerState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    @OptIn(FlowPreview::class)
    fun initSearch() {
        viewModelScope.launch {
            _searchQuery
                .debounce(200) // 200ms delay
                .distinctUntilChanged()
                .filter { it.isNotBlank() }
                .collect { query ->
                    performSearch()
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
        _searchQuery.value = query

        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(
                results = emptyList(),
                hasSearched = false,
                errorMessage = null
            )
        }
    }

    private fun performSearch() {
        viewModelScope.launch {
            repository.searchSections().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true,
                            errorMessage = null
                        )
                    }
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            results = resource.data.results.last().content,
                            isLoading = false,
                            errorMessage = null,
                            hasSearched = true
                        )
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = resource.message,
                            hasSearched = true
                        )
                    }
                }
            }
        }
    }

    fun playAudio(content: ContentItem, url: String) {
        val mediaItem = MediaItem.fromUri(url.toUri())
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()

        _exoPlayerState.value = _exoPlayerState.value.copy(
            playingUri = url,
            playingContent = content,
            playingPosition = 0,
            isPlaying = true,
            loadingAudioContent = true
        )
        exoPlayer.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                _exoPlayerState.value = _exoPlayerState.value.copy(duration = exoPlayer.duration, loadingAudioContent = false)
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    _exoPlayerState.value = _exoPlayerState.value.copy(duration = exoPlayer.duration, loadingAudioContent = false)
                }
            }
        })

        startProgressUpdates()
    }

    private fun startProgressUpdates() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (true) {
                _exoPlayerState.value = _exoPlayerState.value.copy(
                    playingPosition = exoPlayer.currentPosition
                )
                delay(250L)
            }
        }
    }

    fun pauseAudio() {
        exoPlayer.pause()
        _exoPlayerState.value = _exoPlayerState.value.copy(isPlaying = false)
    }

    fun closeAudio() {
        exoPlayer.stop()
        _exoPlayerState.value = _exoPlayerState.value.copy(isPlaying = false, playingContent = null, playingUri = null)
    }

    fun resumeAudio() {
        exoPlayer.play()
        _exoPlayerState.value = _exoPlayerState.value.copy(isPlaying = true)
    }

    override fun onCleared() {
        super.onCleared()
        progressJob?.cancel()
        exoPlayer.release()
    }
}