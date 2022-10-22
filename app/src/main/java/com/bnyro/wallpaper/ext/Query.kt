package com.bnyro.wallpaper.ext

fun Query(query: () -> Unit) {
    Thread(query).start()
}
