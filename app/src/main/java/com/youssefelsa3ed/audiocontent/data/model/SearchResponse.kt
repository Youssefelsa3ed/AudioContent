package com.youssefelsa3ed.audiocontent.data.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("sections")
    val results: List<Section>
)