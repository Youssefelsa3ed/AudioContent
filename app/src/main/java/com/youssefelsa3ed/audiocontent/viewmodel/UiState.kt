package com.youssefelsa3ed.audiocontent.viewmodel

import com.youssefelsa3ed.audiocontent.data.model.ContentItem
import com.youssefelsa3ed.audiocontent.data.model.Pagination
import com.youssefelsa3ed.audiocontent.data.model.Section

data class HomeUiState(
    val sections: List<Section> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val errorMessage: String? = null,
    val pagination: Pagination? = null,
    val currentPage: Int = 1,
    val canLoadMore: Boolean = false
)

data class SearchUiState(
    val query: String = "",
    val results: List<ContentItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val hasSearched: Boolean = false
)

data class ExoPlayerState(
    val playingUri: String? = null,
    val playingContent: ContentItem? = null,
    val playingPosition: Long = 0,
    val duration: Long = 0,
    val isPlaying: Boolean = false,
    val loadingAudioContent: Boolean = false
)