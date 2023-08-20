package com.bnyro.wallpaper.api.re.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Data(
    @JsonProperty("after") val after: String? = null,
    @JsonProperty("children") val children: ArrayList<Children>? = arrayListOf()
)
