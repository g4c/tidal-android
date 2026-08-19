package com.tidal.android

import android.app.Application
import com.tidal.android.download.TidalDownloadManager
import com.tidal.android.repository.TidalRepository
import com.tidal.android.service.MockTidalService

class TidalApplication : Application() {
    companion object {
        lateinit var repository: TidalRepository
        lateinit var downloadManager: TidalDownloadManager
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize service and repository
        val tidalService = MockTidalService()
        repository = TidalRepository(tidalService)
        downloadManager = TidalDownloadManager(this)
    }
}