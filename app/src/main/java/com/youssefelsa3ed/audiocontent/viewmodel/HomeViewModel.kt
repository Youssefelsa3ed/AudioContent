package com.youssefelsa3ed.audiocontent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youssefelsa3ed.audiocontent.data.model.Pagination
import com.youssefelsa3ed.audiocontent.data.model.Section
import com.youssefelsa3ed.audiocontent.data.repository.AudioContentRepository
import com.youssefelsa3ed.audiocontent.data.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val sections: List<Section> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val errorMessage: String? = null,
    val pagination: Pagination? = null,
    val currentPage: Int = 1,
    val canLoadMore: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AudioContentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

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
}