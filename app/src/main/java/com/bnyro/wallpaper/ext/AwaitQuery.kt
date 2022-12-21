package com.bnyro.wallpaper.ext

fun <T> awaitQuery(block: () -> T): T {
    var result: T? = null
    Thread {
        result = block.invoke()
    }.apply {
        start()
        join()
    }
    return result!!
}
