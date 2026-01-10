package com.bnyro.wallpaper

import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.AreaChart
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Pix
import androidx.compose.material.icons.filled.ScreenLockLandscape
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.filled.WaterDamage
import androidx.compose.material.icons.filled.WaterDrop
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import com.bnyro.wallpaper.db.DatabaseHolder
import com.bnyro.wallpaper.util.WallpaperApiWrapper
import com.bnyro.wallpaper.util.Preferences
import net.youapps.wallpaper_apis.RetrofitHelper
import net.youapps.wallpaper_apis.bi.BingApi
import net.youapps.wallpaper_apis.le.LemmyApi
import net.youapps.wallpaper_apis.na.NasaPotdApi
import net.youapps.wallpaper_apis.ow.OwallsApi
import net.youapps.wallpaper_apis.pb.PixabayApi
import net.youapps.wallpaper_apis.ps.PicsumApi
import net.youapps.wallpaper_apis.px.GooglePixelApi
import net.youapps.wallpaper_apis.re.RedditApi
import net.youapps.wallpaper_apis.sp.MicrosoftSpotlightApi
import net.youapps.wallpaper_apis.us.UnsplashApi
import net.youapps.wallpaper_apis.wh.WallhavenApi
import net.youapps.wallpaper_apis.wi.WikipediaPotdApi
import net.youapps.wallpaper_apis.ze.ZedgeApi

class App : Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()

        Preferences.init(this)

        DatabaseHolder.create(this)
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(true)
            .respectCacheHeaders(false)
            .diskCache(
                DiskCache.Builder()
                    .directory(
                        cacheDir.resolve("coil")
                    )
                    .maxSizeBytes(
                        Preferences.getString(
                            Preferences.diskCacheKey,
                            Preferences.defaultDiskCacheSize.toString()
                        ).toLong()
                    )
                    .build()
            )
            .okHttpClient { RetrofitHelper.okHttpClient }
            .build()
    }

    companion object {
        val apis by lazy {
            listOf(
                WallpaperApiWrapper(WallhavenApi(), Icons.Default.Landscape),
                WallpaperApiWrapper(OwallsApi(), Icons.Default.Air),
                WallpaperApiWrapper(UnsplashApi(), Icons.Default.WaterDrop),
                WallpaperApiWrapper(PixabayApi(), Icons.Default.Palette),
                WallpaperApiWrapper(ZedgeApi(), Icons.Default.ScreenLockLandscape),
                WallpaperApiWrapper(BingApi(), Icons.Default.Nightlight),
                WallpaperApiWrapper(RedditApi(), Icons.Default.Forum),
                WallpaperApiWrapper(LemmyApi(), Icons.Default.Book),
                WallpaperApiWrapper(GooglePixelApi(), Icons.Default.Pix),
                WallpaperApiWrapper(MicrosoftSpotlightApi(), Icons.Default.LightMode),
                WallpaperApiWrapper(NasaPotdApi(), Icons.Default.Star),
                WallpaperApiWrapper(WikipediaPotdApi(), Icons.Default.Today),
                WallpaperApiWrapper(PicsumApi(), Icons.Default.AreaChart)
            )
        }
    }
}
