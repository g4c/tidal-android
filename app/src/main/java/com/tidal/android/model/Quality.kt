package com.tidal.android.model

import java.io.Serializable

/**
 * Audio quality modes for playback and download
 */
enum class QualityMode(val displayName: String, val apiValue: String, val bitrate: Int = 0) : Serializable {
    // Playback/Download qualities
    LOW("Low (96 kbps)", "LOW", 96),
    NORMAL("Normal (320 kbps)", "HIGH", 320),
    LOSSLESS("Lossless (FLAC 1411 kbps)", "LOSSLESS", 1411),
    HI_RES("Hi-Res (MQA/FLAC up to 9216 kbps)", "HI_RES", 9216);

    fun isBetterThan(other: QualityMode): Boolean {
        return this.bitrate > other.bitrate
    }

    fun isLossless(): Boolean {
        return this == LOSSLESS || this == HI_RES
    }
}

/**
 * Audio quality information for tracks and albums
 */
data class AudioQualityInfo(
    val mode: QualityMode = QualityMode.NORMAL,
    val isBitDepthAvailable: Boolean = false,
    val bitDepth: Int? = null,
    val sampleRate: Int? = null,
    val isHiRes: Boolean = false,
    val audioModes: List<String> = emptyList() // e.g., ["STEREO", "DOLBY_ATMOS"]
) : Serializable {

    fun getHiResMarker(): String {
        return if (isHiRes) {
            if (bitDepth != null && sampleRate != null) {
                "HiRes ${bitDepth}bit/${sampleRate}kHz"
            } else {
                "HiRes Audio"
            }
        } else {
            ""
        }
    }
}

/**
 * Download quality selection
 */
data class DownloadQuality(
    val mode: QualityMode,
    val maxConcurrentDownloads: Int = 3
) : Serializable
