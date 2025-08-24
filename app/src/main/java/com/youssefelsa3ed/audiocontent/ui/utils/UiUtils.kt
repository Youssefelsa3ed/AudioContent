package com.youssefelsa3ed.audiocontent.ui.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

object UiUtils {

    fun getDuration(duration: Int): String = when {
        duration > 3600 -> "${duration / 3600}h ${(duration % 3600) / 60}m"
        duration > 60 -> "${duration / 60}m"
        else -> "${duration}s"
    }

    fun getReleaseDate(releaseDate: String): String {
        return try {
            val dateTime = LocalDateTime.ofInstant(
                Instant.parse(releaseDate), ZoneId.systemDefault()
            )
            val now = LocalDateTime.now()
            val minutes = ChronoUnit.MINUTES.between(dateTime, now)
            val hours = ChronoUnit.HOURS.between(dateTime, now)
            val days = ChronoUnit.DAYS.between(dateTime, now)
            val weeks = ChronoUnit.WEEKS.between(dateTime, now)

            when {
                minutes < 60 -> "$minutes minutes ago"
                hours < 24 -> "$hours hours ago"
                days == 1L -> "yesterday"
                days in 2..6 -> "$days days ago"
                weeks < 5 -> "$weeks weeks ago"
                else -> {
                    val formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.getDefault())
                    dateTime.format(formatter)
                }
            }
        } catch (_: Exception) {
            releaseDate
        }
    }
}