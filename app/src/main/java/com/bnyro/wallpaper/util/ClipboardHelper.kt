package com.bnyro.wallpaper.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

class ClipboardHelper(context: Context) {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    fun write(text: String) {
        clipboardManager.setPrimaryClip(ClipData.newPlainText("Color copied", text))
    }
}
