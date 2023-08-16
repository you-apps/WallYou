package com.bnyro.wallpaper.api.re.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Children(
    @JsonProperty("data") val childdata: ChildData
)
