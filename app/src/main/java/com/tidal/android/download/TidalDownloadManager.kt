package com.tidal.android.download

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tidal.android.model.QualityMode
import com.tidal.android.model.Track
import com.tidal.android.util.Constants
import kotlinx.coroutines.*
import java.io.File

data class DownloadTask(
    val id: String,
    val track: Track,
    val quality: QualityMode = QualityMode.NORMAL,
    val status: DownloadStatus = DownloadStatus.PENDING,
    val progress: Int = 0,
    val downloadedBytes: Long = 0L,
    val totalBytes: Long = 0L
)

enum class DownloadStatus {
    PENDING, DOWNLOADING, PAUSED, COMPLETED, FAILED, CANCELLED
}

class TidalDownloadManager(private val context: Context) {

    private val qualityManager = DownloadQualityManager(context)
    private val activeDownloads = MutableLiveData<List<DownloadTask>>(emptyList())
    private val downloadScope = CoroutineScope(Dispatchers.IO + Job())
    private val downloadQueue = mutableListOf<DownloadTask>()
    private val completedDownloads = mutableListOf<DownloadTask>()

    fun getActiveDownloads(): LiveData<List<DownloadTask>> = activeDownloads

    /**
     * Download tracks with specified quality
     */
    suspend fun downloadTracks(
        tracks: List<Track>,
        quality: QualityMode = qualityManager.getSelectedQuality()
    ) {
        val downloadQuality = qualityManager.getDownloadQuality()
        val maxConcurrent = downloadQuality.maxConcurrentDownloads

        tracks.forEach { track ->
            val task = DownloadTask(
                id = "${track.id}_${System.currentTimeMillis()}",
                track = track,
                quality = quality
            )
            downloadQueue.add(task)
        }

        // Start downloads respecting max concurrent limit
        processDownloadQueue(maxConcurrent)
    }

    /**
     * Process download queue with concurrency control
     */
    private suspend fun processDownloadQueue(maxConcurrent: Int) {
        downloadScope.launch {
            val activeBatches = mutableListOf<Deferred<Unit>>()

            while (downloadQueue.isNotEmpty() || activeBatches.isNotEmpty()) {
                // Start new downloads if under limit
                while (activeBatches.size < maxConcurrent && downloadQueue.isNotEmpty()) {
                    val task = downloadQueue.removeAt(0)
                    val deferred = async {
                        performDownload(task)
                    }
                    activeBatches.add(deferred)
                }

                // Wait for at least one to complete
                if (activeBatches.isNotEmpty()) {
                    val completed = selectCompleted(*activeBatches.toTypedArray(), onTimeout = { })
                    activeBatches.removeAll { it == completed }
                }
            }
        }
    }

    /**
     * Perform actual download of a track
     */
    private suspend fun performDownload(task: DownloadTask) {
        val updatedTask = task.copy(status = DownloadStatus.DOWNLOADING)
        updateDownloadStatus(updatedTask)

        try {
            // Get stream URL based on quality
            val streamUrl = getStreamUrlForQuality(task.track, task.quality)
            if (streamUrl.isEmpty()) {
                updateDownloadStatus(task.copy(status = DownloadStatus.FAILED))
                return
            }

            // Download file
            val downloadDir = File(
                context.getExternalFilesDir(null),
                Constants.DOWNLOAD_FOLDER
            )
            downloadDir.mkdirs()

            val fileName = "${task.track.artist.name} - ${task.track.title}.flac"
            val outputFile = File(downloadDir, fileName)

            // Simulate download (in real implementation, use HttpClient to download)
            delay(2000) // Simulate download time

            val completed = updatedTask.copy(
                status = DownloadStatus.COMPLETED,
                progress = 100,
                downloadedBytes = 50 * 1024 * 1024, // 50MB
                totalBytes = 50 * 1024 * 1024
            )
            updateDownloadStatus(completed)
            completedDownloads.add(completed)
        } catch (e: Exception) {
            updateDownloadStatus(task.copy(status = DownloadStatus.FAILED))
        }
    }

    /**
     * Get stream URL based on quality selection
     */
    private suspend fun getStreamUrlForQuality(
        track: Track,
        quality: QualityMode
    ): String {
        // This would call the API with the appropriate quality parameter
        return try {
            when (quality) {
                QualityMode.HI_RES -> "https://stream.tidal.com/hi-res/${track.id}"
                QualityMode.LOSSLESS -> "https://stream.tidal.com/lossless/${track.id}"
                else -> "https://stream.tidal.com/normal/${track.id}"
            }
        } catch (e: Exception) {
            ""
        }
    }

    private fun updateDownloadStatus(task: DownloadTask) {
        val current = activeDownloads.value?.toMutableList() ?: mutableListOf()
        val existing = current.indexOfFirst { it.id == task.id }
        if (existing >= 0) {
            current[existing] = task
        } else {
            current.add(task)
        }
        activeDownloads.value = current
    }

    suspend fun pauseDownloads() {
        downloadScope.cancel()
    }

    suspend fun resumeDownloads() {
        // Recreate scope if needed
    }

    suspend fun cancelDownload(taskId: String) {
        val current = activeDownloads.value?.toMutableList() ?: return
        val cancelled = current.find { it.id == taskId }?.copy(status = DownloadStatus.CANCELLED)
        if (cancelled != null) {
            val index = current.indexOfFirst { it.id == taskId }
            current[index] = cancelled
            activeDownloads.value = current
        }
    }
}
