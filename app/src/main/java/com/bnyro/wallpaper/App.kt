package com.bnyro.wallpaper

import android.app.Application
import com.bnyro.wallpaper.util.PrefHolder

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        PrefHolder.init(this)
    }
}
