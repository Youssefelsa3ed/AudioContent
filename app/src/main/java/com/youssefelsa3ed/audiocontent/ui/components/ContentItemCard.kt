package com.youssefelsa3ed.audiocontent.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.youssefelsa3ed.audiocontent.data.model.ContentItem
import com.youssefelsa3ed.audiocontent.data.model.EpisodeContent
import com.youssefelsa3ed.audiocontent.data.model.PodcastContent
import com.youssefelsa3ed.audiocontent.ui.utils.UiUtils
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun ContentItemCard(
    item: ContentItem,
    modifier: Modifier = Modifier,
    isSquare: Boolean,
    isBigSquare: Boolean,
    onEpisodeClicked: (audioUrl: String, episode: EpisodeContent) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val width = if (isBigSquare) 300.dp else 200.dp
        Card(
            modifier = Modifier
                .let {
                    when {
                        isBigSquare -> it
                            .height(300.dp)
                        isSquare -> it.height(150.dp)
                        else -> it
                    }
                }
                .aspectRatio(1f),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box {
                AsyncImage(
                    model = item.thumbnail,
                    contentDescription = item.title,
                    modifier = Modifier.fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                if (item is PodcastContent) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.BottomCenter)
                            .background(brush =
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                                ))
                            .padding(8.dp),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                        Text(
                            text = "${item.episodeCount ?: 0} episodes",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        if (item is PodcastContent)
            return@Column

        Text(
            text = item.title,
            style = if (isBigSquare) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(width)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(width)
        ) {
            item.duration?.let { duration ->
                val displayDuration = UiUtils.getDuration(duration)
                Row(
                    modifier = Modifier
                        .background(
                            shape = RoundedCornerShape(30.dp),
                            color = Color.Black.copy(alpha = 0.5f)
                        )
                        .padding(vertical = 2.dp, horizontal = 8.dp)
                ) {
                    item.contentPlayableUrl?.let {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.White,
                            modifier = Modifier.clickable {
                                onEpisodeClicked(it, item as? EpisodeContent ?: return@clickable)
                            }
                        )
                    }
                    Text(
                        text = displayDuration,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                    )
                }
            }
            item.releaseDate?.let {
                Text(
                    text = UiUtils.getReleaseDate(it),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun TwoLineGridItem(
    item: ContentItem,
    modifier: Modifier = Modifier,
    onEpisodeClicked: (audioUri: String, episode: EpisodeContent) -> Unit
) {
    Row(
        modifier = modifier.padding(end = 16.dp).fillMaxWidth().height(100.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.thumbnail,
            contentDescription = item.title,
            modifier = Modifier
                .height(100.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.weight(1f)
                .fillMaxHeight()
                .padding(start = 8.dp)
        ) {
            item.releaseDate?.let {
                Text(
                    text = UiUtils.getReleaseDate(it),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1
                )
            }
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            item.duration?.let { duration ->
                val displayDuration = UiUtils.getDuration(duration)
                Row(
                    modifier = Modifier
                        .background(
                            shape = RoundedCornerShape(30.dp),
                            color = Color.Black.copy(alpha = 0.5f)
                        )
                        .padding(vertical = 2.dp, horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    item.contentPlayableUrl?.let {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.White,
                            modifier = Modifier.clickable {
                                onEpisodeClicked(it, item as? EpisodeContent ?: return@clickable)
                            }
                        )
                    }
                    Text(
                        text = displayDuration,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                    )
                }
            }
        }
    }
}

@Composable
fun CardStackQueue(
    items: List<ContentItem>,
    modifier: Modifier = Modifier,
    onEpisodeClicked: (audioUrl :String, episode: EpisodeContent) -> Unit
) {
    var cards by remember { mutableStateOf(items) }
    val maxVisible = 4
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .height(100.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = ((maxVisible) * 8).dp)
                    .weight(1f)
            ) {
                val topCard = cards.first()
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Text(
                        text = topCard.title,
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = 14.sp
                    )
                    Row {
                        topCard.duration?.let {
                            Text(
                                text = UiUtils.getDuration(it),
                                style = MaterialTheme.typography.labelMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.Red
                            )
                        }
                        topCard.releaseDate?.let {
                            Text(
                                text = UiUtils.getReleaseDate(it),
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1
                            )
                        }
                    }
                }
                topCard.contentPlayableUrl?.let {
                    IconButton(
                        onClick = {
                            onEpisodeClicked(it, topCard as? EpisodeContent ?: return@IconButton)
                        },
                        modifier = Modifier.align(Alignment.BottomEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.White,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .background(
                                    color = Color.Black.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(40.dp)
                                )
                        )
                    }
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
            model = card.thumbnail,
            contentDescription = card.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
    }
}


