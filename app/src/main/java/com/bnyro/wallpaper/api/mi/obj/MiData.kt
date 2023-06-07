package com.bnyro.wallpaper.api.mi.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class MiData(
    @JsonProperty("bizids") val bizIds: List<String>? = null,
    @JsonProperty("child_item_count") val childItemCount: Int? = null,
    @JsonProperty("exparam") val exParam: MiExParam? = null,
    @JsonProperty("favor_count") val favorCount: Int? = null,

    @JsonProperty("id") val id: String? = null,
    @JsonProperty("images") val images: List<MiImages>,

    @JsonProperty("is_favor") val isFavor: Int? = null,
    @JsonProperty("item_type") val itemType: String? = null,
    @JsonProperty("j_actions") val jActions: Any? = null,

    @JsonProperty("meta") val meta: MiMeta? = null,
    @JsonProperty("rcm_info") val rcmInfo: MiRcmInfo? = null,
    @JsonProperty("times") val times: MiTimes? = null
)
