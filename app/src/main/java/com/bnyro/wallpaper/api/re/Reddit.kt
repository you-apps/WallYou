package com.bnyro.wallpaper.api.re

import com.bnyro.wallpaper.api.re.obj.RedditResp
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

@Suppress("ktlint:standard:max-line-length")
private const val USER_AGENT_HEADER =
    "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36"

interface Reddit {
    @Headers(USER_AGENT_HEADER)
    @GET("r/{subreddit}/{sort}.json")
    suspend fun getRedditData(
        @Path("subreddit") subreddit: String,
        @Path("sort") sort: String,
        @Query("t") time: String? = null,
        @Query("after") after: String? = null
    ): RedditResp
}
