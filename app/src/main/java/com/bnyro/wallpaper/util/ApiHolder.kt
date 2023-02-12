package com.bnyro.wallpaper.util

import com.bnyro.wallpaper.api.ow.OwApi
import com.bnyro.wallpaper.api.ps.PsApi
import com.bnyro.wallpaper.api.us.UsApi
import com.bnyro.wallpaper.api.wh.WhApi

object ApiHolder {
    val whApi = WhApi()
    val psApi = PsApi()
    val owApi = OwApi()
    val usApi = UsApi()
}
