package com.tidal.android.download

import android.content.Context
import android.os.Environment
import androidx.documentfile.provider.DocumentFile
import com.tidal.android.model.Track
import com.tidal.android.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import java.util.concurrent.CopyOnWriteArrayList

class TidalDownloadManager(private val context: Context) {

    private val activeTasks = CopyOnWriteArrayList<DownloadTask>()
    private val downloadListeners = mutableListOf<DownloadListener>()

    interface DownloadListener {
        fun onProgressUpdate(task: DownloadTask)
        fun onTaskCompleted(task: DownloadTask)
        fun onTaskFailed(task: DownloadTask)
    }

    suspend fun downloadTracks(tracks: List<Track>) {
        withContext(Dispatchers.IO) {
            for (track in tracks) {
                downloadTrack(track)
            }
        }
    }

    private suspend fun downloadTrack(track: Track) {
        withContext(Dispatchers.IO) {
            val taskId = UUID.randomUUID().toString()
            val task = DownloadTask(
                id = taskId,
                track = track,
                status = DownloadTask.Status.DOWNLOADING
            )
            activeTasks.add(task)

            try {
                // Create download directory
                val downloadDir = getDownloadDirectory()
                if (!downloadDir.exists()) {
                    downloadDir.mkdirs()
                }

                // Create file path
                val fileName = "${track.artist.name} - ${track.title}.m4a"
                val file = File(downloadDir, fileName)

                // In a real implementation, download the file from track.url
                // For now, this is a placeholder
                task.status = DownloadTask.Status.COMPLETED
                task.filePath = file.absolutePath
                notifyTaskCompleted(task)
            } catch (e: Exception) {
                task.status = DownloadTask.Status.FAILED
                task.errorMessage = e.message
                notifyTaskFailed(task)
            } finally {
                activeTasks.remove(task)
            }
        }
    }

    fun cancelTask(taskId: String) {
        activeTasks.removeAll { it.id == taskId }
    }

    fun getActiveTasks(): List<DownloadTask> {
        return activeTasks.toList()
    }

    fun addListener(listener: DownloadListener) {
        downloadListeners.add(listener)
    }

    fun removeListener(listener: DownloadListener) {
        downloadListeners.remove(listener)
    }

    fun shutdown() {
        activeTasks.clear()
        downloadListeners.clear()
    }

    private fun getDownloadDirectory(): File {
        return File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC),
            Constants.DOWNLOAD_FOLDER
        )
    }

    private fun notifyProgressUpdate(task: DownloadTask) {
        downloadListeners.forEach { it.onProgressUpdate(task) }
    }

    private fun notifyTaskCompleted(task: DownloadTask) {
        downloadListeners.forEach { it.onTaskCompleted(task) }
    }

    private fun notifyTaskFailed(task: DownloadTask) {
        downloadListeners.forEach { it.onTaskFailed(task) }
    }
}