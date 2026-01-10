package net.youapps.wallpaper_apis.le

import net.youapps.wallpaper_apis.le.obj.LemmyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Lemmy {
    @GET("api/v3/post/list")
    suspend fun getWallpapers(
        @Query("page") page: Int,
        @Query("community_name") communityName: String,
        @Query("limit") limit: Int = 10,
        @Query("type_") type: String = "All",
        @Query("sort") sort: String = "Active"
    ): LemmyResponse
}
