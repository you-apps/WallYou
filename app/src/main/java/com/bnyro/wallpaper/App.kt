package com.bnyro.wallpaper

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import com.bnyro.wallpaper.util.PrefHolder

class App : Application(), ImageLoaderFactory {
    private val defaultCacheSize = 128L * 1024L * 1024L

    override fun onCreate() {
        super.onCreate()

        PrefHolder.init(this)
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
                        PrefHolder.Preferences.getString(
                            PrefHolder.diskCacheKey,
                            defaultCacheSize.toString()
                        )?.toLong() ?: defaultCacheSize
                    )
                    .build()
            )
            .build()
    }
}
