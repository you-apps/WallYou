package net.youapps.wallpaper_apis.ow

import kotlinx.serialization.json.JsonObject
import retrofit2.http.GET

interface OWalls {
    @GET("tanujnotes/85e2d0343ace71e76615ac346fbff82b/raw")
    suspend fun getAll(): JsonObject
}
