package com.youssefelsa3ed.audiocontent.data.model

import com.google.gson.annotations.SerializedName

sealed class ContentItem {
    abstract val name: String
    abstract val description: String?
    abstract val avatarUrl: String?
    abstract val duration: Int?
    abstract val score: String?
}

data class PodcastContent(
    @SerializedName("podcast_id")
    val podcastId: String,
    @SerializedName("name")
    override val name: String,
    @SerializedName("description")
    override val description: String?,
    @SerializedName("avatar_url")
    override val avatarUrl: String?,
    @SerializedName("episode_count")
    val episodeCount: Int?,
    @SerializedName("duration")
    override val duration: Int?,
    @SerializedName("language")
    val language: String?,
    @SerializedName("priority")
    val priority: String?,
    @SerializedName("popularityScore")
    val popularityScore: String?,
    @SerializedName("score")
    override val score: String?
) : ContentItem()

data class EpisodeContent(
    @SerializedName("episode_id")
    val episodeId: String,
    @SerializedName("name")
    override val name: String,
    @SerializedName("description")
    override val description: String?,
    @SerializedName("avatar_url")
    override val avatarUrl: String?,
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
    val audioUrl: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("podcast_id")
    val podcastId: String?,
    @SerializedName("score")
    override val score: String?,
    @SerializedName("podcastPopularityScore")
    val podcastPopularityScore: Int?,
    @SerializedName("podcastPriority")
    val podcastPriority: Int?
) : ContentItem()

data class AudioBookContent(
    @SerializedName("audiobook_id")
    val audiobookId: String,
    @SerializedName("name")
    override val name: String,
    @SerializedName("author_name")
    val authorName: String?,
    @SerializedName("description")
    override val description: String?,
    @SerializedName("avatar_url")
    override val avatarUrl: String?,
    @SerializedName("duration")
    override val duration: Int?,
    @SerializedName("language")
    val language: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("score")
    override val score: String?
) : ContentItem()

data class AudioArticleContent(
    @SerializedName("article_id")
    val articleId: String,
    @SerializedName("name")
    override val name: String,
    @SerializedName("author_name")
    val authorName: String?,
    @SerializedName("description")
    override val description: String?,
    @SerializedName("avatar_url")
    override val avatarUrl: String?,
    @SerializedName("duration")
    override val duration: Int?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("score")
    override val score: String?
) : ContentItem()