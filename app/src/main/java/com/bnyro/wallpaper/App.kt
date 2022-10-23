package com.bnyro.wallpaper

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import com.bnyro.wallpaper.db.DatabaseHolder
import com.bnyro.wallpaper.util.Preferences

class App : Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()

        Preferences.init(this)

        DatabaseHolder().create(this)
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
                        )?.toLong() ?: Preferences.defaultDiskCacheSize
                    )
                    .build()
            )
            .build()
    }
}
