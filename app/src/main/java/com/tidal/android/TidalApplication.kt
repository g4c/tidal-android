package com.tidal.android

import android.app.Application
import com.tidal.android.download.TidalDownloadManager
import com.tidal.android.repository.TidalRepository
import com.tidal.android.service.impl.TidalServiceImpl

class TidalApplication : Application() {

    companion object {
        lateinit var repository: TidalRepository
        lateinit var downloadManager: TidalDownloadManager
        private lateinit var tidalService: TidalServiceImpl
    }

    override fun onCreate() {
        super.onCreate()
        initializeDependencies()
    }

    private fun initializeDependencies() {
        // Initialize Tidal Service with OAuth2
        tidalService = TidalServiceImpl(this)

        // Initialize Repository
        repository = TidalRepository(tidalService)

        // Initialize Download Manager
        downloadManager = TidalDownloadManager(this)
    }
}