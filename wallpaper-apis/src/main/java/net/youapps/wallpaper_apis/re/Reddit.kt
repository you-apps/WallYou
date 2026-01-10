package net.youapps.wallpaper_apis.re

import net.youapps.wallpaper_apis.re.obj.RedditResp
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface Reddit {
    @GET("r/{subreddit}/{sort}.json")
    suspend fun getRedditData(
        @Path("subreddit") subreddit: String,
        @Path("sort") sort: String,
        @Query("t") time: String? = null,
        @Query("after") after: String? = null
    ): RedditResp
}
