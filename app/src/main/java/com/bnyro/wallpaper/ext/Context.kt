package com.bnyro.wallpaper.ext

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun Context.toastFromMainThread(text: String?, length: Int = Toast.LENGTH_SHORT)
= withContext(Dispatchers.Main) {
    Toast.makeText(this@toastFromMainThread, text, length).show()
}

fun Context.toast(text: String?, length: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, text, length).show()