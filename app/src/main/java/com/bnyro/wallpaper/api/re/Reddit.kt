package com.bnyro.wallpaper.api.re

import com.bnyro.wallpaper.api.re.obj.RedditResp
import retrofit2.http.GET
import retrofit2.http.Headers
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
