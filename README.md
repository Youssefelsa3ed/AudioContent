# Audio Content App - Technical Documentation

## 📋 Project Overview

The Audio Content App is an Android application developed in Kotlin using Jetpack Compose to display diverse audio content including podcasts, episodes, audiobooks, and audio articles. The app features two main screens: Home and Search, with integrated audio playback functionality using ExoPlayer.

## 🏗️ Solution Architecture & Implementation

### MVVM Architecture Implementation

The application follows the MVVM pattern to separate concerns across different layers:

#### 1. **Model Layer**
- `ContentItem`: Abstract sealed class representing different content types
- `PodcastContent`, `EpisodeContent`, `AudioBookContent`, `AudioArticleContent`: Concrete implementations for each content type
- `HomeSectionsResponse`, `SearchResponse`: API response models
- `Section`: Model representing content sections with different display types

#### 2. **Data Layer**
```kotlin
// Repository Pattern for data source management
@Singleton
class AudioContentRepository @Inject constructor(
    private val homeApiService: HomeApiService,
    private val searchApiService: SearchApiService
)
```

**Implemented Features:**
- **Resource Wrapper**: Encapsulates loading, success, and error states
- **Flow-based API**: Uses Kotlin Flow for reactive data handling
- **Comprehensive Error Handling**: Proper exception handling and error propagation

#### 3. **Presentation Layer**
- `HomeViewModel`, `SearchViewModel`: State management and business logic
- Compose UI Components: Reusable UI components for consistent design

### Core Feature Implementation

#### 1. **Infinite Scrolling**
```kotlin
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
```

#### 2. **Debounced Search**
```kotlin
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
```

#### 3. **Audio Playback**
- ExoPlayer integration for audio file playback
- Bottom Audio Player for playback controls
- Real-time playback progress tracking

#### 4. **Dynamic Layout Types**
- **Square Grid**
- **Two Lines Grid**
- **Big Square**
- **Card Stack Queue**: Swipeable card stack display

## 💡 Custom JSON Deserialization

Implemented custom JSON deserializer to handle different content types:

```kotlin
class ContentItemDeserializer : JsonDeserializer<ContentItem> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): ContentItem {
        val jsonObject = json.asJsonObject
        return when {
            jsonObject.has("episode_id") -> context.deserialize(json, EpisodeContent::class.java)
            jsonObject.has("podcast_id") -> context.deserialize(json, PodcastContent::class.java)
            jsonObject.has("audiobook_id") -> context.deserialize(json, AudioBookContent::class.java)
            jsonObject.has("article_id") -> context.deserialize(json, AudioArticleContent::class.java)
            else -> throw IllegalArgumentException("Unknown content type")
        }
    }
}
```

## 🎯 Dependency Injection with Hilt

Efficient dependency management using Hilt:

#### NetworkModule
- Retrofit setup for different API endpoints
- OkHttp configuration with logging
- Custom Gson converter injection with deserializer

#### PlayerModule
- ExoPlayer as Singleton provider
- Player lifecycle management

## 🚀 Advanced Features Implementation

### 1. **Card Stack Queue Component**
Custom component for swipeable card stack display:
- Visible card state management
- Drag and drop interactions
- Smooth transitions between cards

### 2. **ExoPlayer State**
Handle audio player state:
```kotlin
data class ExoPlayerState(
    val playingUri: String? = null,
    val playingContent: ContentItem? = null,
    val playingPosition: Long = 0,
    val duration: Long = 0,
    val isPlaying: Boolean = false,
    val loadingAudioContent: Boolean = false
)
```

## 🔧 Challenges Faced and Solutions

### 1. **Managing Different Content Types**
**Challenge:** Handling JSON responses containing different content types in the same list.

**Solution Implemented:**
- Used Sealed Classes for ContentItem
- Developed custom ContentItemDeserializer

### 2. **Infinite Scrolling with Loading States**
**Challenge:** Managing initial loading and pagination states while preventing redundant requests.

**Solution Implemented:**
```kotlin
// Separated loading states
data class HomeUiState(
    val isLoading: Boolean = false,      // Initial loading
    val isLoadingMore: Boolean = false,  // Pagination loading
    val canLoadMore: Boolean = false     // Load more control
)
```

### 3. **ExoPlayer State Synchronization**
**Challenge:** Handle audio player state with real-time progress tracking.

**Solution Implemented:**
- StateFlow for state sharing
- Coroutine Job for periodic position updates
- Player.Listener for state change tracking

### 4. **Optimized Search Implementation**
**Challenge:** Implementing efficient search while avoiding unnecessary requests.

**Solution Implemented:**
```kotlin
_searchQuery
    .debounce(200)           // 200ms delay
    .distinctUntilChanged()  // Prevent duplicate requests
    .filter { it.isNotBlank() } // Ignore empty values
```

## 💡 Improvement Suggestions and Future Development

### 1. **Performance Optimizations**

##### A) Implement Caching with Room
##### B) Image Loading Optimization

### 2. **User Experience Improvements**

#### A) Skeleton Loading Implementation
#### B) Pull-to-Refresh

### 4. **Additional Feature Suggestions**

#### A) Background Playback
#### B) Playlist and Queue Management
#### C) Offline Download Support
### 5. **Comprehensive Testing Strategy**

#### A) More Unit Tests
#### B) UI Tests

## 🎯 Conclusion

The Audio Content App has been successfully developed using Android best practices with Kotlin and Jetpack Compose. The application provides a smooth user experience with efficient state management and performance optimization. The implemented solutions are scalable and maintainable, with significant potential for future improvements and development.

**Strengths:**
- Clean and organized architecture
- Efficient state management
- Responsive user interface
- Performance optimizations
- Comprehensive error handling

**Future Opportunities:**
- Implement caching mechanisms
- Enhanced error handling
- Expanded audio player features
- Comprehensive testing suite
- Background playback capabilities

**Technical Highlights:**
- Custom JSON deserializer for polymorphic content
- Advanced UI components with drag interactions
- Efficient infinite scrolling implementation
- Debounced search with duplicate prevention
- Shared audio player state management

The application demonstrates proficient use of modern Android development techniques and provides a solid foundation for future enhancements and feature additions.
