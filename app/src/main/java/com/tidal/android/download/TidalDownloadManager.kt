package com.tidal.android.download

import android.content.Context
import android.os.Environment
import com.tidal.android.model.Track
import com.tidal.android.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.min

class TidalDownloadManager(private val context: Context) {

    private val downloadTasks = ConcurrentHashMap<String, DownloadTask>()
    private val _activeDownloads = MutableStateFlow<List<DownloadTask>>(emptyList())
    private var isPaused = false

    /**
     * Get flow of active downloads
     */
    fun getActiveDownloads(): Flow<List<DownloadTask>> = _activeDownloads

    /**
     * Download multiple tracks
     */
    suspend fun downloadTracks(tracks: List<Track>) {
        tracks.forEach { track ->
            val task = DownloadTask(
                id = generateTaskId(),
                trackId = track.id,
                title = track.title,
                artist = track.artist?.name ?: "Unknown Artist",
                status = DownloadStatus.PENDING
            )
            downloadTasks[task.id] = task
        }
        updateActiveDownloads()
        startDownloadQueue()
    }

    /**
     * Cancel specific download
     */
    suspend fun cancelDownload(taskId: String) {
        downloadTasks[taskId]?.let {
            downloadTasks[taskId] = it.copy(status = DownloadStatus.CANCELLED)
            updateActiveDownloads()
        }
    }

    /**
     * Pause all downloads
     */
    fun pauseDownloads() {
        isPaused = true
        downloadTasks.forEach { (_, task) ->
            if (task.status == DownloadStatus.DOWNLOADING) {
                downloadTasks[task.id] = task.copy(status = DownloadStatus.PAUSED)
            }
        }
        updateActiveDownloads()
    }

    /**
     * Resume all downloads
     */
    fun resumeDownloads() {
        isPaused = false
        downloadTasks.forEach { (_, task) ->
            if (task.status == DownloadStatus.PAUSED) {
                downloadTasks[task.id] = task.copy(status = DownloadStatus.PENDING)
            }
        }
        updateActiveDownloads()
    }

    /**
     * Get download progress for specific track
     */
    fun getDownloadProgress(trackId: String): Int {
        return downloadTasks.values.firstOrNull { it.trackId == trackId }?.progress ?: 0
    }

    /**
     * Update download progress
     */
    suspend fun updateProgress(taskId: String, downloadedSize: Long, fileSize: Long) {
        downloadTasks[taskId]?.let {
            val progress = if (fileSize > 0) ((downloadedSize * 100) / fileSize).toInt() else 0
            downloadTasks[taskId] = it.copy(
                downloadedSize = downloadedSize,
                fileSize = fileSize,
                progress = progress
            )
            updateActiveDownloads()
        }
    }

    /**
     * Mark download as completed
     */
    suspend fun markCompleted(taskId: String) {
        downloadTasks[taskId]?.let {
            downloadTasks[taskId] = it.copy(
                status = DownloadStatus.COMPLETED,
                downloadedAt = System.currentTimeMillis()
            )
            updateActiveDownloads()
        }
    }

    /**
     * Mark download as failed
     */
    suspend fun markFailed(taskId: String) {
        downloadTasks[taskId]?.let {
            downloadTasks[taskId] = it.copy(status = DownloadStatus.FAILED)
            updateActiveDownloads()
        }
    }

    /**
     * Get download directory
     */
    private fun getDownloadDirectory(): File {
        val musicDir = File(
            context.getExternalFilesDir(Environment.DIRECTORY_MUSIC),
            Constants.DOWNLOAD_FOLDER
        )
        if (!musicDir.exists()) {
            musicDir.mkdirs()
        }
        return musicDir
    }

    /**
     * Get file path for track
     */
    fun getTrackFilePath(trackId: String, title: String): File {
        val dir = getDownloadDirectory()
        val filename = "${title.sanitizeFilename()}.m4a"
        return File(dir, filename)
    }

    /**
     * Start download queue processing
     */
    private suspend fun startDownloadQueue() {
        val pending = downloadTasks.values.filter { it.status == DownloadStatus.PENDING }
        val toProcess = pending.take(Constants.MAX_CONCURRENT_DOWNLOADS)

        toProcess.forEach { task ->
            if (!isPaused) {
                downloadTasks[task.id] = task.copy(status = DownloadStatus.DOWNLOADING)
                // Actual download logic would be implemented here
                // For now, mark as completed
                markCompleted(task.id)
            }
        }

        updateActiveDownloads()
    }

    /**
     * Update active downloads flow
     */
    private fun updateActiveDownloads() {
        val active = downloadTasks.values.filter {
            it.status == DownloadStatus.DOWNLOADING || it.status == DownloadStatus.PENDING
        }
        _activeDownloads.value = active
    }

    /**
     * Generate unique task ID
     */
    private fun generateTaskId(): String {
        return "task_${System.currentTimeMillis()}_${(0..9999).random()}"
    }

    /**
     * Sanitize filename
     */
    private fun String.sanitizeFilename(): String {
        return this.replace(Regex("[<>:\"|?*]"), "_")
            .replace("/", "_")
            .replace("\\", "_")
            .take(255)
    }
}