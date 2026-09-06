package net.youapps.wallpaper_apis.re

import net.youapps.wallpaper_apis.re.obj.RedditListingResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface Reddit {
    @GET("r/{subreddit}/{sort}.json")
    suspend fun getRedditJson(
        @Path("subreddit") subreddit: String,
        @Path("sort") sort: String,
        @Query("limit") limit: Int = 25,
        @Query("t") time: String? = null,
        @Query("after") after: String? = null,
        @Query("raw_json") rawJson: Int = 1,
        @Header("Cookie") cookie: String? = null
    ): Response<RedditListingResponse>

    @GET("r/{subreddit}/{sort}.rss")
    suspend fun getRedditData(
        @Path("subreddit") subreddit: String,
        @Path("sort") sort: String,
        @Query("t") time: String? = null,
        @Query("after") after: String? = null,
        @Header("Cookie") cookie: String? = null
    ): ResponseBody
}
