package com.youssefelsa3ed.audiocontent.ui.utils

object Utils {

    fun getDuration(duration: Int): String = when {
        duration > 3600 -> "${duration / 3600}h ${(duration % 3600) / 60}m"
        duration > 60 -> "${duration / 60}m"
        else -> "${duration}s"
    }
}