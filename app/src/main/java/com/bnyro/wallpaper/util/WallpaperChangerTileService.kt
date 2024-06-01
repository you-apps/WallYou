package com.bnyro.wallpaper.util

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import androidx.core.os.postDelayed
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

@RequiresApi(Build.VERSION_CODES.N)
class WallpaperChangerTileService : TileService() {
    override fun onStartListening() {
        super.onStartListening()

        qsTile.state = Tile.STATE_INACTIVE
        qsTile.updateTile()
    }

    override fun onClick() {
        super.onClick()

        val oneTimeJob = OneTimeWorkRequestBuilder<BackgroundWorker>()
            .build()

        WorkManager.getInstance(this)
            .enqueueUniqueWork(TILE_WORKER_KEY, ExistingWorkPolicy.REPLACE, oneTimeJob)

        qsTile.state = Tile.STATE_ACTIVE
        qsTile.updateTile()

        Handler(Looper.getMainLooper()).postDelayed(3000) {
            qsTile.state = Tile.STATE_INACTIVE
            qsTile.updateTile()
        }
    }

    companion object {
        private const val TILE_WORKER_KEY = "tile_worker_key"
    }
}