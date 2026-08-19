package com.tidal.android.download

import java.io.Serializable

data class DownloadTask(
    val id: String,
    val trackId: String,
    val title: String,
    val artist: String,
    val fileSize: Long = 0L,
    val downloadedSize: Long = 0L,
    val status: DownloadStatus = DownloadStatus.PENDING,
    val progress: Int = 0,
    val downloadedAt: Long = 0L
) : Serializable {
    val progressPercent: Int
        get() = if (fileSize > 0) ((downloadedSize * 100) / fileSize).toInt() else 0
}

enum class DownloadStatus {
    PENDING,
    DOWNLOADING,
    PAUSED,
    COMPLETED,
    FAILED,
    CANCELLED
}