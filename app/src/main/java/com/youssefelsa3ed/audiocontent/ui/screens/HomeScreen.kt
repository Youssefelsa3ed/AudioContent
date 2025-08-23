package com.youssefelsa3ed.audiocontent.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.youssefelsa3ed.audiocontent.ui.components.BottomAudioPlayer
import com.youssefelsa3ed.audiocontent.ui.components.SectionView
import com.youssefelsa3ed.audiocontent.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToSearch: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    // Load more content when reaching the end
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleItemIndex ->
                if (lastVisibleItemIndex != null &&
                    lastVisibleItemIndex >= uiState.sections.size - 2 &&
                    uiState.canLoadMore &&
                    !uiState.isLoadingMore) {
                    viewModel.loadMoreSections()
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Audio Content") },
                actions = {
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAudioPlayer(
                uiState = uiState,
                onPlayPauseClicked = {
                    if (uiState.isPlaying)
                        viewModel.pauseAudio()
                    else
                        viewModel.resumeAudio()
                },
                closePlayer = { viewModel.closeAudio() }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading && uiState.sections.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.errorMessage != null && uiState.sections.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = uiState.errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.retry() }) {
                            Text("Retry")
                        }
                    }
                }
                uiState.sections.isNotEmpty() -> {
                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(uiState.sections) { section ->
                            SectionView(section = section) { audioUri, episode ->
                                if (uiState.isPlaying && uiState.playingUri == audioUri)
                                    return@SectionView
                                viewModel.playAudio(episode, audioUri)
                            }
                        }

                        if (uiState.isLoadingMore) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
                else -> {
                    Text(
                        text = "No content available",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            if (uiState.errorMessage != null && uiState.sections.isNotEmpty())
                LaunchedEffect(uiState.errorMessage) {
                    println("Error loading more content: ${uiState.errorMessage}")
                }
        }
    }
}