package com.bnyro.wallpaper.api.le.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class LemmyPost(
    @JsonProperty("ap_id") val postUrl: String = "",
    @JsonProperty("body") val body: String = "",
    @JsonProperty("community_id") val communityId: Int = 0,
    @JsonProperty("creator_id") val creatorId: Int = 0,
    @JsonProperty("deleted") val deleted: Boolean = false,
    @JsonProperty("featured_community") val featuredCommunity: Boolean = false,
    @JsonProperty("featured_local") val featuredLocal: Boolean = false,
    @JsonProperty("id") val id: Int = 0,
    @JsonProperty("language_id") val languageId: Int = 0,
    @JsonProperty("local") val local: Boolean = false,
    @JsonProperty("locked") val locked: Boolean = false,
    @JsonProperty("name") val name: String = "",
    @JsonProperty("nsfw") val nsfw: Boolean = false,
    @JsonProperty("published") val published: String = "",
    @JsonProperty("removed") val removed: Boolean = false,
    @JsonProperty("thumbnail_url") val thumbnailUrl: String? = null,
    @JsonProperty("url") val url: String? = null
)
