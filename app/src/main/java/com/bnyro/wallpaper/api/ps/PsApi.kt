package com.bnyro.wallpaper.api.ps

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pix
import com.bnyro.wallpaper.api.Api
import com.bnyro.wallpaper.db.obj.Wallpaper
import com.bnyro.wallpaper.util.RetrofitHelper

class PsApi : Api() {
    override val name: String = "Picsum"
    override val baseUrl: String = "https://picsum.photos"
    override val icon = Icons.Default.Pix
    private val maxPage = 34

    private val api = RetrofitHelper.create<Picsum>(baseUrl)

    override suspend fun getWallpapers(page: Int): List<Wallpaper> {
        return api.getWallpapers(maxPage + 1 - page).map {
            Wallpaper(
                imgSrc = it.downloadUrl!!,
                author = it.author,
                resolution = "${it.width}x${it.height}",
                url = it.url
            )
        }
    }

    override suspend fun getRandomWallpaperUrl(): String {
        return "$baseUrl/1080/1920"
    }
}
