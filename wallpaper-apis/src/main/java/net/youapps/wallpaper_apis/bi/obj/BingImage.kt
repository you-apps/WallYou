package net.youapps.wallpaper_apis.bi.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BingImage(
    @SerialName("bot") val bot: Int? = null,
    @SerialName("copyright") val copyright: String? = null,
    @SerialName("copyrightlink") val copyrightLink: String? = null,
    @SerialName("drk") val drk: Int? = null,
    @SerialName("enddate") val endDate: String? = null,
    @SerialName("fullstartdate") val fullStartDate: String? = null,
    @SerialName("hsh") val hsh: String? = null,
    @SerialName("quiz") val quiz: String? = null,
    @SerialName("startdate") val startDate: String? = null,
    @SerialName("title") val title: String = "",
    @SerialName("top") val top: Int? = null,
    @SerialName("url") val url: String = "",
    @SerialName("urlbase") val urlBase: String = "",
    @SerialName("wp") val wp: Boolean = false
)
