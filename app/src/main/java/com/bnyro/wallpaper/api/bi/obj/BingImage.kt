package com.bnyro.wallpaper.api.bi.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class BingImage(
    @JsonProperty("bot") val bot: Int? = null,
    @JsonProperty("copyright") val copyright: String? = null,
    @JsonProperty("copyrightlink") val copyrightLink: String? = null,
    @JsonProperty("drk") val drk: Int? = null,
    @JsonProperty("enddate") val endDate: String? = null,
    @JsonProperty("fullstartdate") val fullStartDate: String? = null,
    @JsonProperty("hsh") val hsh: String? = null,
    @JsonProperty("quiz") val quiz: String? = null,
    @JsonProperty("startdate") val startDate: String? = null,
    @JsonProperty("title") val title: String = "",
    @JsonProperty("top") val top: Int? = null,
    @JsonProperty("url") val url: String = "",
    @JsonProperty("urlbase") val urlBase: String = "",
    @JsonProperty("wp") val wp: Boolean = false
)
