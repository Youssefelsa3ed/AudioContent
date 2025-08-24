package com.youssefelsa3ed.audiocontent.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.youssefelsa3ed.audiocontent.R
import com.youssefelsa3ed.audiocontent.viewmodel.ExoPlayerState

@Composable
fun BottomAudioPlayer(
    exoPlayerState: ExoPlayerState,
    onPlayPauseClicked: () -> Unit,
    closePlayer: () -> Unit
) {
    if (exoPlayerState.playingUri != null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .background(Color.DarkGray)
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImage(
                    exoPlayerState.playingContent?.thumbnail ?: "",
                    exoPlayerState.playingContent?.title ?: "",
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = exoPlayerState.playingContent?.title ?: "Now Playing",
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )

                if (exoPlayerState.loadingAudioContent)
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp)
                    )
                else
                    IconButton(onClick = {
                        if (exoPlayerState.playingPosition >= exoPlayerState.duration)
                            closePlayer.invoke()
                        else
                            onPlayPauseClicked.invoke()
                    }) {
                        if (exoPlayerState.playingPosition >= exoPlayerState.duration) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Play",
                                tint = Color.White
                            )
                        }
                        else if (exoPlayerState.isPlaying)
                            Image(
                                painterResource(R.drawable.outline_pause_circle_24),
                                contentDescription = "Pause",
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                        else
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.White
                            )
                    }
            }

            LinearProgressIndicator(
                progress = {
                    if (exoPlayerState.playingPosition > 0) {
                        (exoPlayerState.playingPosition.toFloat() / exoPlayerState.duration.toFloat())
                            .coerceIn(0f, 1f)
                    } else 0f
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = Color.Green
            )
        }
    }
}