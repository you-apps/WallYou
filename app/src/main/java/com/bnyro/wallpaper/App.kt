package com.bnyro.wallpaper

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import com.bnyro.wallpaper.api.bi.BiApi
import com.bnyro.wallpaper.api.le.LeApi
import com.bnyro.wallpaper.api.na.NaApi
import com.bnyro.wallpaper.api.ow.OwApi
import com.bnyro.wallpaper.api.pb.PixabayApi
import com.bnyro.wallpaper.api.ps.PsApi
import com.bnyro.wallpaper.api.px.PxApi
import com.bnyro.wallpaper.api.re.ReApi
import com.bnyro.wallpaper.api.sp.SpApi
import com.bnyro.wallpaper.api.us.UsApi
import com.bnyro.wallpaper.api.wh.WhApi
import com.bnyro.wallpaper.api.wi.WiAPi
import com.bnyro.wallpaper.api.ze.ZeApi
import com.bnyro.wallpaper.db.DatabaseHolder
import com.bnyro.wallpaper.util.Preferences

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
            .build()
    }

    companion object {
        val apis =
            listOf(WhApi(), OwApi(), UsApi(), PixabayApi(), BiApi(), ReApi(), LeApi(), PxApi(), SpApi(), NaApi(), WiAPi(), PsApi(),
                ZeApi())
    }
}
