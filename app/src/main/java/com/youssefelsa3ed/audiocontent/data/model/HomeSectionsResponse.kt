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
    val type: String,
    @SerializedName("content_type")
    val contentType: String,
    @SerializedName("order")
    val order: String,
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
            jsonObject.has("episode_id") -> context.deserialize(json, EpisodeContent::class.java)
            jsonObject.has("podcast_id") -> context.deserialize(json, PodcastContent::class.java)
            jsonObject.has("audiobook_id") -> context.deserialize(json, AudioBookContent::class.java)
            jsonObject.has("article_id") -> context.deserialize(json, AudioArticleContent::class.java)
            else -> throw IllegalArgumentException("Unknown content type")
        }
    }
}

enum class SectionType(val title: String) {
    Square("square"),
    HorizontalList("horizontal_list"),
    TwoLineGrid("2_lines_grid"),
    BigSquare("big_square"),
    Queue("queue");
}