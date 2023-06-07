package com.bnyro.wallpaper.api.mi.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class MiImages(
    @JsonProperty("cl_url") val clUrl: MiCurl? = null,
    @JsonProperty("text_layers") val textLayers: List<MiTextLayers>? = null
)
