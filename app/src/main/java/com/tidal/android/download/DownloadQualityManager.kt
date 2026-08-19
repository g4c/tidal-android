package com.tidal.android.download

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.tidal.android.model.DownloadQuality
import com.tidal.android.model.QualityMode
import com.tidal.android.util.Constants

class DownloadQualityManager(context: Context) {

    private val prefs = context.getSharedPreferences(
        "${Constants.PREF_NAME}_quality",
        Context.MODE_PRIVATE
    )

    val selectedQuality = MutableLiveData<QualityMode>(getSelectedQuality())

    init {
        selectedQuality.observeForever { quality ->
            saveSelectedQuality(quality)
        }
    }

    /**
     * Get currently selected download quality
     */
    fun getSelectedQuality(): QualityMode {
        val qualityName = prefs.getString("selected_quality", QualityMode.NORMAL.name)
        return try {
            QualityMode.valueOf(qualityName ?: QualityMode.NORMAL.name)
        } catch (e: Exception) {
            QualityMode.NORMAL
        }
    }

    /**
     * Save selected download quality
     */
    fun saveSelectedQuality(quality: QualityMode) {
        prefs.edit().putString("selected_quality", quality.name).apply()
        selectedQuality.value = quality
    }

    /**
     * Get available quality options (user can only select qualities their subscription supports)
     */
    fun getAvailableQualities(): List<QualityMode> {
        return listOf(
            QualityMode.NORMAL,
            QualityMode.LOSSLESS,
            QualityMode.HI_RES
        )
    }

    /**
     * Get max concurrent downloads based on quality
     */
    fun getMaxConcurrentDownloads(quality: QualityMode): Int {
        return when (quality) {
            QualityMode.HI_RES -> 1 // Hi-Res downloads should be sequential
            QualityMode.LOSSLESS -> 2
            else -> Constants.MAX_CONCURRENT_DOWNLOADS
        }
    }

    /**
     * Get download quality with concurrency settings
     */
    fun getDownloadQuality(): DownloadQuality {
        val quality = getSelectedQuality()
        return DownloadQuality(
            mode = quality,
            maxConcurrentDownloads = getMaxConcurrentDownloads(quality)
        )
    }

    /**
     * Check if Hi-Res downloads are enabled
     */
    fun isHiResDownloadsEnabled(): Boolean {
        return getSelectedQuality() == QualityMode.HI_RES
    }

    /**
     * Check if lossless downloads are enabled
     */
    fun isLosslessDownloadsEnabled(): Boolean {
        val quality = getSelectedQuality()
        return quality == QualityMode.LOSSLESS || quality == QualityMode.HI_RES
    }
}
