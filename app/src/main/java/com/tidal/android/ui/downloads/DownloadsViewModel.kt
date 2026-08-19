package com.tidal.android.ui.downloads

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tidal.android.download.DownloadTask
import com.tidal.android.download.TidalDownloadManager
import kotlinx.coroutines.launch

class DownloadsViewModel(
    private val downloadManager: TidalDownloadManager
) : ViewModel() {

    private val _activeDownloads = MutableLiveData<List<DownloadTask>>(emptyList())
    val activeDownloads: LiveData<List<DownloadTask>> = _activeDownloads

    init {
        observeDownloads()
    }

    private fun observeDownloads() {
        viewModelScope.launch {
            downloadManager.getActiveDownloads().collect { downloads ->
                _activeDownloads.value = downloads
            }
        }
    }

    suspend fun pauseDownloads() {
        downloadManager.pauseDownloads()
    }

    suspend fun resumeDownloads() {
        downloadManager.resumeDownloads()
    }

    suspend fun cancelDownload(taskId: String) {
        downloadManager.cancelDownload(taskId)
    }
}

class DownloadsViewModelFactory(
    private val downloadManager: TidalDownloadManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return DownloadsViewModel(downloadManager) as T
    }
}
