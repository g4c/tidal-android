package com.tidal.android.ui.downloads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tidal.android.download.TidalDownloadManager

class DownloadsViewModelFactory(private val downloadManager: TidalDownloadManager) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DownloadsViewModel::class.java)) {
            return DownloadsViewModel(downloadManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}