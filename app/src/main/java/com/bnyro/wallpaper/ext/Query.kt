package com.bnyro.wallpaper.ext

fun query(query: () -> Unit) {
    Thread(query).start()
}
