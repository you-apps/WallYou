package com.bnyro.wallpaper.ext

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

fun Any.toJsonObject(): JsonNode {
    val mapper = ObjectMapper()
    return mapper.readTree(mapper.writeValueAsString(this))
}
