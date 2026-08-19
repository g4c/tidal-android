package com.tidal.android.download

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class DownloadTaskTest {

    @Test
    fun testDownloadProgressCalculation() {
        val task = DownloadTask(
            id = "task_1",
            trackId = "track_1",
            title = "Test Song",
            artist = "Test Artist",
            fileSize = 1000L,
            downloadedSize = 500L
        )

        assertEquals(50, task.progressPercent)
    }

    @Test
    fun testDownloadProgressZeroFileSize() {
        val task = DownloadTask(
            id = "task_1",
            trackId = "track_1",
            title = "Test Song",
            artist = "Test Artist",
            fileSize = 0L,
            downloadedSize = 0L
        )

        assertEquals(0, task.progressPercent)
    }

    @Test
    fun testDownloadProgressComplete() {
        val task = DownloadTask(
            id = "task_1",
            trackId = "track_1",
            title = "Test Song",
            artist = "Test Artist",
            fileSize = 1000L,
            downloadedSize = 1000L
        )

        assertEquals(100, task.progressPercent)
    }

    @Test
    fun testDownloadStatusPending() {
        val task = DownloadTask(
            id = "task_1",
            trackId = "track_1",
            title = "Test Song",
            artist = "Test Artist"
        )

        assertEquals(DownloadStatus.PENDING, task.status)
    }

    @Test
    fun testDownloadStatusTransitions() {
        var task = DownloadTask(
            id = "task_1",
            trackId = "track_1",
            title = "Test Song",
            artist = "Test Artist"
        )

        assertTrue(task.status == DownloadStatus.PENDING)

        task = task.copy(status = DownloadStatus.DOWNLOADING)
        assertEquals(DownloadStatus.DOWNLOADING, task.status)

        task = task.copy(status = DownloadStatus.COMPLETED)
        assertEquals(DownloadStatus.COMPLETED, task.status)
    }
}