package com.bnyro.wallpaper.api.mi.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class MiMeta(
    @JsonProperty("album_info") val albumInfo: MiAlbumInfo? = null,
    @JsonProperty("all_tags") val allTags: List<String>? = null,
    @JsonProperty("categories") val categories: List<MiVendor>? = null,
    @JsonProperty("cp") val cp: MiMedia? = null,
    @JsonProperty("desc") val desc: String? = null,
    @JsonProperty("is_all") val isAll: Int? = null,
    @JsonProperty("link_type") val linkType: Int? = null,
    @JsonProperty("media") val media: MiMedia? = null,
    @JsonProperty("tags") val tags: List<String>? = null,
    @JsonProperty("title") val title: String? = null,
    @JsonProperty("vendor") val vendor: MiVendor? = null
)
