package com.bnyro.wallpaper.api.mi.obj

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MiRcmInfo(
    val expid: String? = null,
    val order_f: Int? = null,
    val rcm_type: String? = null,
    val reqid: String? = null,
    val traceid: String? = null
)
