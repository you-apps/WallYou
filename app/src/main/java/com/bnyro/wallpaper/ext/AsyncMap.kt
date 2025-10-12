package com.bnyro.wallpaper.ext

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

suspend fun <A, B> List<A>.amap(f: suspend (A) -> B): List<Deferred<B>> = map { coroutineScope { async { f(it) } } }