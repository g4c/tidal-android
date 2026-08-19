package com.tidal.android.ui.downloads

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tidal.android.download.DownloadTask
import com.tidal.android.download.TidalDownloadManager
import com.tidal.android.model.Track
import kotlinx.coroutines.launch

class DownloadsViewModel(private val downloadManager: TidalDownloadManager) : ViewModel() {

    private val _activeDownloads = MutableLiveData<List<DownloadTask>>()
    val activeDownloads: LiveData<List<DownloadTask>> = _activeDownloads

    private val _downloadProgress = MutableLiveData<Map<String, Int>>()
    val downloadProgress: LiveData<Map<String, Int>> = _downloadProgress

    init {
        viewModelScope.launch {
            downloadManager.getActiveDownloads().collect {
                _activeDownloads.value = it
            }
        }
    }

    fun startDownloads(tracks: List<Track>) {
        viewModelScope.launch {
            downloadManager.downloadTracks(tracks)
        }
    }

    fun cancelDownload(trackId: String) {
        viewModelScope.launch {
            downloadManager.cancelDownload(trackId)
        }
    }

    fun pauseDownloads() {
        downloadManager.pauseDownloads()
    }

    fun resumeDownloads() {
        downloadManager.resumeDownloads()
    }
}

class DownloadsViewModelFactory(private val downloadManager: TidalDownloadManager) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DownloadsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DownloadsViewModel(downloadManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}