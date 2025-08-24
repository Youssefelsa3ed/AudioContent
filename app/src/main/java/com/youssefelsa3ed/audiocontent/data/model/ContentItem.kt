package com.youssefelsa3ed.audiocontent.data.model

import com.google.gson.annotations.SerializedName

sealed class ContentItem {
    abstract val title: String
    abstract val description: String?
    abstract val thumbnail: String?
    abstract val duration: Int?
    abstract val score: String?
    open val order: String? = "0"
    open val releaseDate: String? = null
    open val contentPlayableUrl: String? = null
}

data class PodcastContent(
    @SerializedName("podcast_id")
    val podcastId: String,
    @SerializedName("name")
    override val title: String,
    @SerializedName("description")
    override val description: String?,
    @SerializedName("avatar_url")
    override val thumbnail: String?,
    @SerializedName("episode_count")
    val episodeCount: Int?,
    @SerializedName("duration")
    override val duration: Int?,
    @SerializedName("language")
    val language: String?,
    @SerializedName("priority")
    override val order: String?,
    @SerializedName("popularityScore")
    val popularityScore: String?,
    @SerializedName("score")
    override val score: String?
) : ContentItem()

data class EpisodeContent(
    @SerializedName("episode_id")
    val episodeId: String,
    @SerializedName("name")
    override val title: String,
    @SerializedName("description")
    override val description: String?,
    @SerializedName("avatar_url")
    override val thumbnail: String?,
    @SerializedName("duration")
    override val duration: Int?,
    @SerializedName("podcast_name")
    val podcastName: String?,
    @SerializedName("author_name")
    val authorName: String?,
    @SerializedName("season_number")
    val seasonNumber: Int?,
    @SerializedName("episode_type")
    val episodeType: String?,
    @SerializedName("number")
    val number: Int?,
    @SerializedName("audio_url")
    override val contentPlayableUrl: String?,
    @SerializedName("release_date")
    override val releaseDate: String?,
    @SerializedName("podcast_id")
    val podcastId: String?,
    @SerializedName("score")
    override val score: String?,
    @SerializedName("podcastPopularityScore")
    val podcastPopularityScore: Int?,
    @SerializedName("podcastPriority")
    override val order: String?
) : ContentItem()

data class AudioBookContent(
    @SerializedName("audiobook_id")
    val audiobookId: String,
    @SerializedName("name")
    override val title: String,
    @SerializedName("author_name")
    val authorName: String?,
    @SerializedName("description")
    override val description: String?,
    @SerializedName("avatar_url")
    override val thumbnail: String?,
    @SerializedName("duration")
    override val duration: Int?,
    @SerializedName("language")
    val language: String?,
    @SerializedName("release_date")
    override val releaseDate: String?,
    @SerializedName("score")
    override val score: String?
) : ContentItem()

data class AudioArticleContent(
    @SerializedName("article_id")
    val articleId: String,
    @SerializedName("name")
    override val title: String,
    @SerializedName("author_name")
    val authorName: String?,
    @SerializedName("description")
    override val description: String?,
    @SerializedName("avatar_url")
    override val thumbnail: String?,
    @SerializedName("duration")
    override val duration: Int?,
    @SerializedName("release_date")
    override val releaseDate: String?,
    @SerializedName("score")
    override val score: String?
) : ContentItem()