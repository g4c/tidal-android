package com.tidal.android.download

import com.tidal.android.model.Track

data class DownloadTask(
    val id: String,
    val track: Track,
    var status: Status = Status.PENDING,
    var progress: Int = 0,
    var totalSize: Long = 0,
    var downloadedSize: Long = 0,
    var errorMessage: String? = null,
    var filePath: String? = null
) {
    enum class Status {
        PENDING, DOWNLOADING, COMPLETED, FAILED, CANCELLED
    }

    fun getSizeFormatted(): String {
        if (totalSize == 0L) {
            return "0 MB"
        }
        return String.format("%.2f MB", totalSize / (1024.0 * 1024.0))
    }

    fun getProgressFormatted(): String {
        return String.format(
            "%.2f MB / %.2f MB",
            downloadedSize / (1024.0 * 1024.0),
            totalSize / (1024.0 * 1024.0)
        )
    }
}