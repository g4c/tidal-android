package com.tidal.android.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Track(
    val id: String,
    val title: String,
    val artist: Artist,
    val album: Album? = null,
    val duration: Int,
    val trackNumber: Int = 0,
    val isExplicit: Boolean = false,
    val isStreamable: Boolean = true,
    @SerializedName("audioQuality")
    val audioQuality: String? = null,
    @SerializedName("audioModes")
    val audioModes: List<String>? = null,
    @SerializedName("isHiRes")
    val isHiRes: Boolean = false,
    @SerializedName("maxBitDepth")
    val maxBitDepth: Int? = null,
    @SerializedName("maxSampleRate")
    val maxSampleRate: Int? = null
) : Serializable {

    fun getQualityInfo(): AudioQualityInfo {
        val mode = when {
            isHiRes -> QualityMode.HI_RES
            audioQuality?.uppercase() == "LOSSLESS" -> QualityMode.LOSSLESS
            else -> QualityMode.NORMAL
        }

        return AudioQualityInfo(
            mode = mode,
            isBitDepthAvailable = maxBitDepth != null,
            bitDepth = maxBitDepth,
            sampleRate = maxSampleRate,
            isHiRes = isHiRes,
            audioModes = audioModes.orEmpty()
        )
    }

    fun getHiResMarker(): String {
        return getQualityInfo().getHiResMarker()
    }
}

data class Artist(
    val id: String,
    val name: String,
    val imageUrl: String? = null
) : Serializable

data class Album(
    val id: String,
    val title: String,
    val artist: Artist,
    val coverUrl: String? = null,
    val releaseDate: String? = null,
    val numberOfTracks: Int,
    @SerializedName("audioQuality")
    val audioQuality: String? = null,
    @SerializedName("audioModes")
    val audioModes: List<String>? = null,
    @SerializedName("isHiRes")
    val isHiRes: Boolean = false,
    @SerializedName("maxBitDepth")
    val maxBitDepth: Int? = null,
    @SerializedName("maxSampleRate")
    val maxSampleRate: Int? = null
) : Serializable {

    fun getQualityInfo(): AudioQualityInfo {
        val mode = when {
            isHiRes -> QualityMode.HI_RES
            audioQuality?.uppercase() == "LOSSLESS" -> QualityMode.LOSSLESS
            else -> QualityMode.NORMAL
        }

        return AudioQualityInfo(
            mode = mode,
            isBitDepthAvailable = maxBitDepth != null,
            bitDepth = maxBitDepth,
            sampleRate = maxSampleRate,
            isHiRes = isHiRes,
            audioModes = audioModes.orEmpty()
        )
    }

    fun getHiResMarker(): String {
        return getQualityInfo().getHiResMarker()
    }
}
