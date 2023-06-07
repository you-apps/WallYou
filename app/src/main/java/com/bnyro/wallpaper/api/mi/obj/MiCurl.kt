package com.bnyro.wallpaper.api.mi.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class MiCurl(
    @JsonProperty("locator") val locator: String? = null,
    @JsonProperty("md5_h") val md5_h: String? = null,
    @JsonProperty("md5_l") val md5_l: String? = null,
    @JsonProperty("md5_m") val md5_m: String? = null,
    @JsonProperty("url_full") val url_full: String? = null,
    @JsonProperty("url_h") val url_h: String? = null,
    @JsonProperty("url_l") val url_l: String? = null,
    @JsonProperty("url_m") val url_m: String? = null,
    @JsonProperty("url_root") val url_root: String? = null
)
