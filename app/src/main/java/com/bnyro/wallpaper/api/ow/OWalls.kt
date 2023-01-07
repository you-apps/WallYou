package com.bnyro.wallpaper.api.ow

import retrofit2.http.GET

interface OWalls {
    @GET("tanujnotes/bf400a269746c5c124a599af040dd82e/raw")
    suspend fun getAll(): Any
}
