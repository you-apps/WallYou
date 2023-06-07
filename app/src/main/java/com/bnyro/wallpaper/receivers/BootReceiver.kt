package com.bnyro.wallpaper.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bnyro.wallpaper.util.WorkerHelper

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        WorkerHelper.enqueue(context, true)
    }
}
