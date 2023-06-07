package com.bnyro.wallpaper.api.mi.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class MiTimes(
    @JsonProperty("begin_t") val begin_t: Int? = null,
    @JsonProperty("create") val create: Int? = null,
    @JsonProperty("end_t") val end_t: Int? = null,
    @JsonProperty("update") val update: Int? = null
)
