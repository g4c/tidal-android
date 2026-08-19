package com.tidal.android.ui.downloads

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tidal.android.download.DownloadTask
import com.tidal.android.download.TidalDownloadManager
import com.tidal.android.model.Track
import com.tidal.android.util.Result
import kotlinx.coroutines.launch

class DownloadsViewModel(private val downloadManager: TidalDownloadManager) : ViewModel() {

    private val _activeTasks = MutableLiveData<List<DownloadTask>>(emptyList())
    val activeTasks: LiveData<List<DownloadTask>> = _activeTasks

    private val _downloadProgress = MutableLiveData<Result<Unit>>()
    val downloadProgress: LiveData<Result<Unit>> = _downloadProgress

    fun startDownloads(tracks: List<Track>) {
        viewModelScope.launch {
            try {
                downloadManager.downloadTracks(tracks)
                updateActiveTasksList()
            } catch (e: Exception) {
                _downloadProgress.value = Result.Error(e)
            }
        }
    }

    fun updateActiveTasksList() {
        _activeTasks.value = downloadManager.getActiveTasks().toList()
    }

    fun cancelDownload(taskId: String) {
        viewModelScope.launch {
            downloadManager.cancelTask(taskId)
            updateActiveTasksList()
        }
    }

    override fun onCleared() {
        super.onCleared()
        downloadManager.shutdown()
    }
}