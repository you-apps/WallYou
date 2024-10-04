package com.bnyro.wallpaper.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class Either<out L, out R> {
    data class Left<out L>(val value: L) : Either<L, Nothing>()
    data class Right<out R>(val value: R) : Either<Nothing, R>()
}

@Composable
fun Either<Int, String>.str(): String = when (this) {
    is Either.Left -> stringResource(value)
    is Either.Right -> value
}