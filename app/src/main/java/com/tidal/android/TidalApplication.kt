package com.tidal.android

import android.app.Application
import com.tidal.android.download.TidalDownloadManager
import com.tidal.android.repository.TidalRepository
import com.tidal.android.service.impl.TidalServiceImpl

class TidalApplication : Application() {
    companion object {
        lateinit var repository: TidalRepository
        lateinit var downloadManager: TidalDownloadManager
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize service and repository with real implementation
        val tidalService = TidalServiceImpl(this)
        repository = TidalRepository(tidalService)
        downloadManager = TidalDownloadManager(this)
    }
}