package com.youssefelsa3ed.audiocontent.ui.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.youssefelsa3ed.audiocontent.data.model.AudioArticleContent
import com.youssefelsa3ed.audiocontent.data.model.AudioBookContent
import com.youssefelsa3ed.audiocontent.data.model.ContentItem
import com.youssefelsa3ed.audiocontent.data.model.EpisodeContent
import com.youssefelsa3ed.audiocontent.data.model.PodcastContent
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun ContentItemCard(
    item: ContentItem,
    modifier: Modifier = Modifier,
    isSquare: Boolean = false,
    isBigSquare: Boolean = false
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .let {
                when {
                    isBigSquare -> it.aspectRatio(1f).height(200.dp)
                    isSquare -> it.aspectRatio(1f)
                    else -> it
                }
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            AsyncImage(
                model = item.avatarUrl,
                contentDescription = item.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        when {
                            isBigSquare -> 140.dp
                            isSquare -> 120.dp
                            else -> 100.dp
                        }
                    )
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.name,
                style = if (isBigSquare) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            val subtitle = when (item) {
                is PodcastContent -> "${item.episodeCount ?: 0} episodes"
                is EpisodeContent -> item.podcastName ?: item.authorName
                is AudioBookContent -> item.authorName
                is AudioArticleContent -> item.authorName
            }

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            item.duration?.let { duration ->
                val displayDuration = when {
                    duration > 3600 -> "${duration / 3600}h ${(duration % 3600) / 60}m"
                    duration > 60 -> "${duration / 60}m"
                    else -> "${duration}s"
                }
                Text(
                    text = displayDuration,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun HorizontalContentItem(
    item: ContentItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.width(180.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = item.avatarUrl,
                contentDescription = item.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.name,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            val subtitle = when (item) {
                is PodcastContent -> "${item.episodeCount ?: 0} episodes"
                is EpisodeContent -> item.podcastName ?: item.authorName
                is AudioBookContent -> item.authorName
                is AudioArticleContent -> item.authorName
            }

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun TwoLineGridItem(
    item: ContentItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.avatarUrl,
                contentDescription = item.name,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                val subtitle = when (item) {
                    is PodcastContent -> "${item.episodeCount ?: 0} episodes"
                    is EpisodeContent -> item.podcastName ?: item.authorName
                    is AudioBookContent -> item.authorName
                    is AudioArticleContent -> item.authorName
                }

                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                item.duration?.let { duration ->
                    val displayDuration = when {
                        duration > 3600 -> "${duration / 3600}h ${(duration % 3600) / 60}m"
                        duration > 60 -> "${duration / 60}m"
                        else -> "${duration}s"
                    }
                    Text(
                        text = displayDuration,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun CardStackQueue(
    initialCards: List<ContentItem>,
    modifier: Modifier = Modifier
) {
    var cards by remember { mutableStateOf(initialCards) }
    val maxVisible = 4
    Card(
        modifier = modifier.fillMaxWidth().height(120.dp).padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
                .padding(8.dp)
                .height(100.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box {
                val visibleCards = cards.take(maxVisible)
                visibleCards.asReversed().forEachIndexed { index, card ->
                    val isTop = index == visibleCards.lastIndex
                    val offsetDp = (index * 10).dp
                    DraggableCardItem(
                        card = card,
                        isTop = isTop,
                        modifier = Modifier.offset(x = offsetDp),
                        onSwiped = {
                            if (isTop) {
                                cards = cards.drop(1) + cards.first()
                            }
                        }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(start = ((maxVisible) * 8).dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val topCard = cards.first()
                Text(
                    text = topCard.name,
                    style = MaterialTheme.typography.titleLarge
                )
                topCard.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun DraggableCardItem(
    card: ContentItem,
    isTop: Boolean,
    modifier: Modifier = Modifier,
    onSwiped: () -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .offset { IntOffset(offsetX.roundToInt(), 0) }
            .then(
                if (isTop) Modifier.draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        if (isTop) offsetX += delta
                    },
                    onDragStopped = {
                        if (abs(offsetX) > 110f)
                            onSwiped()
                        offsetX = 0f
                    }
                ) else Modifier
            )
            .size(100.dp)
            .clip(RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = card.avatarUrl,
            contentDescription = card.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
    }
}