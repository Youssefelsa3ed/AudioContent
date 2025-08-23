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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youssefelsa3ed.audiocontent.data.model.Section

@Composable
fun SectionView(
    section: Section,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = section.name,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        when (section.type) {
            "horizontal_list" -> {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(section.content) { item ->
                        HorizontalContentItem(item = item)
                    }
                }
            }
            "2_lines_grid" -> {
                LazyHorizontalGrid(
                    rows = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height((200).dp)
                ) {
                    items(section.content) { item ->
                        TwoLineGridItem(item = item)
                    }
                }
            }
            "square" -> {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.height(100.dp)
                ) {
                    items(section.content) { item ->
                        ContentItemCard(
                            item = item,
                            isSquare = true
                        )
                    }
                }
            }
            "big_square" -> {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.height(200.dp)
                ) {
                    items(section.content) { item ->
                        ContentItemCard(
                            item = item,
                            isBigSquare = true
                        )
                    }
                }
            }
            "queue" -> CardStackQueue(initialCards = section.content)
        }
    }
}