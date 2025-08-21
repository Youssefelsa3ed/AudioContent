package com.youssefelsa3ed.audiocontent.data.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

data class HomeSectionsResponse(
    @SerializedName("sections")
    val sections: List<Section>,
    @SerializedName("pagination")
    val pagination: Pagination
)

data class Section(
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String, // "square", "2_lines_grid", "big_square", "queue
    @SerializedName("content_type")
    val contentType: String, // "podcast", "episode", "audio_book", "audio_article"
    @SerializedName("order")
    val order: Int,
    @SerializedName("content")
    val content: List<ContentItem>
)

data class Pagination(
    @SerializedName("next_page")
    val nextPage: String?,
    @SerializedName("total_pages")
    val totalPages: Int
)

class ContentItemDeserializer : JsonDeserializer<ContentItem> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): ContentItem {
        val jsonObject = json.asJsonObject

        return when {
            jsonObject.has("podcast_id") -> context.deserialize(json, PodcastContent::class.java)
            jsonObject.has("episode_id") -> context.deserialize(json, EpisodeContent::class.java)
            jsonObject.has("audiobook_id") -> context.deserialize(json, AudioBookContent::class.java)
            jsonObject.has("article_id") -> context.deserialize(json, AudioArticleContent::class.java)
            else -> throw IllegalArgumentException("Unknown content type")
        }
    }
}