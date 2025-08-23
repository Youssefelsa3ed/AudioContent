package com.youssefelsa3ed.audiocontent.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youssefelsa3ed.audiocontent.data.model.Section
import com.youssefelsa3ed.audiocontent.data.model.SectionType
import com.youssefelsa3ed.audiocontent.ui.utils.Utils

@Composable
fun SectionView(
    section: Section,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = section.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            if (section.type == SectionType.Queue.title) {
                val totalDuration = section.content.sumOf { it.duration ?: 0 }
                val displayDuration = Utils.getDuration(totalDuration)
                Text(
                    text = "${section.content.size} Episodes, $displayDuration",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        val items = section.content.sortedBy { it.order }
        when (section.type.replace(" ", "_")) {
            SectionType.HorizontalList.title -> {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items) { item ->
                        HorizontalContentItem(item = item)
                    }
                }
            }
            SectionType.TwoLineGrid.title -> {
                LazyHorizontalGrid(
                    rows = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height((200).dp)
                ) {
                    items(items) { item ->
                        TwoLineGridItem(item = item)
                    }
                }
            }
            SectionType.Square.title -> {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.height(100.dp)
                ) {
                    items(items) { item ->
                        ContentItemCard(
                            item = item,
                            isSquare = true
                        )
                    }
                }
            }
            SectionType.BigSquare.title -> {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.height(200.dp)
                ) {
                    items(items) { item ->
                        ContentItemCard(
                            item = item,
                            isBigSquare = true
                        )
                    }
                }
            }
            SectionType.Queue.title -> CardStackQueue(items)
        }
    }
}