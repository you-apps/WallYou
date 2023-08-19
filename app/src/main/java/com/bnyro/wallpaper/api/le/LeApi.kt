package com.bnyro.wallpaper.api.le

import com.bnyro.wallpaper.api.CommunityApi
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.util.RetrofitBuilder

class LeApi : CommunityApi() {
    override val name: String = "Lemmy"
    override val baseUrl: String = "https://lemmy.ml"

    override val defaultCommunityName: String = "apocalypticart@feddit.de"

    private val api = RetrofitBuilder.create(baseUrl, Lemmy::class.java)

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        return api.getWallpapers(page, getCommunityName()).posts.filter {
            !it.post.thumbnailUrl.isNullOrEmpty()
        }.map {
            Wallpaper(
                imgSrc = it.post.thumbnailUrl!!,
                thumb = "${it.post.thumbnailUrl}?format=jpg&thumbnail=1080",
                title = it.post.name,
                url = it.post.postUrl,
                author = it.creator.name,
                creationDate = it.post.published,
            )
        }
    }

    override suspend fun getRandomWallpaperUrl(): String? = getWallpapers(1).randomOrNull()?.imgSrc
}