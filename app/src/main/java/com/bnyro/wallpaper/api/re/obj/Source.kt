package com.bnyro.wallpaper.api.re.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Source(
    @JsonProperty("url") val url: String? = null,
    @JsonProperty("width") val width: Int? = null,
    @JsonProperty("height") val height: Int? = null
) {
    val imgUrl
        get() = url?.replace("&amp;", "&")
}
