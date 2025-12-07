package com.bnyro.wallpaper

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import com.bnyro.wallpaper.api.bi.BingApi
import com.bnyro.wallpaper.api.le.LemmyApi
import com.bnyro.wallpaper.api.na.NasaPotdApi
import com.bnyro.wallpaper.api.ow.OwallsApi
import com.bnyro.wallpaper.api.pb.PixabayApi
import com.bnyro.wallpaper.api.ps.PicsumApi
import com.bnyro.wallpaper.api.px.GooglePixelApi
import com.bnyro.wallpaper.api.re.RedditApi
import com.bnyro.wallpaper.api.sp.MicrosoftSpotlightApi
import com.bnyro.wallpaper.api.us.UnsplashApi
import com.bnyro.wallpaper.api.wh.WallhavenApi
import com.bnyro.wallpaper.api.wi.WikipediaPotdApi
import com.bnyro.wallpaper.api.ze.ZedgeApi
import com.bnyro.wallpaper.db.DatabaseHolder
import com.bnyro.wallpaper.util.Preferences
import com.bnyro.wallpaper.util.RetrofitHelper

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
        val apis =
            listOf(
                WallhavenApi(),
                OwallsApi(),
                UnsplashApi(),
                PixabayApi(),
                ZedgeApi(),
                BingApi(),
                RedditApi(),
                LemmyApi(),
                GooglePixelApi(),
                MicrosoftSpotlightApi(),
                NasaPotdApi(),
                WikipediaPotdApi(),
                PicsumApi()
            )
    }
}
