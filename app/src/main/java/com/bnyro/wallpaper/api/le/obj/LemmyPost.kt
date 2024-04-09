package com.bnyro.wallpaper.api.le.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class LemmyPost(
    @JsonProperty("ap_id") val postUrl: String = "",
    @JsonProperty("name") val name: String = "",
    @JsonProperty("published") val published: String = "",
    @JsonProperty("thumbnail_url") val thumbnailUrl: String? = null,
)
