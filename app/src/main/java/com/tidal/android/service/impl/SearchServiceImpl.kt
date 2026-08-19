package com.tidal.android.service.impl

import com.tidal.android.model.Album
import com.tidal.android.model.Artist
import com.tidal.android.model.Track
import com.tidal.android.service.api.TidalApiClient
import com.tidal.android.util.Constants
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

class SearchServiceImpl(private val apiClient: TidalApiClient) {

    /**
     * Search tracks using new TIDAL API v2 filter-based format
     * Endpoint: /searchResults?filter[query]={query}&include=tracks,artists,albums
     */
    suspend fun searchTracks(query: String, limit: Int = Constants.DEFAULT_SEARCH_LIMIT): List<Track> {
        return try {
            val response = apiClient.searchTracks(
                query = query,
                limit = limit,
                include = "tracks,artists,albums"
            )
            response.data.filterIsInstance<Track>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Search artists using new TIDAL API v2 filter-based format
     */
    suspend fun searchArtists(query: String, limit: Int = Constants.DEFAULT_SEARCH_LIMIT): List<Artist> {
        return try {
            val response = apiClient.searchArtists(
                query = query,
                limit = limit,
                include = "artists"
            )
            response.data.filterIsInstance<Artist>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Search albums using new TIDAL API v2 filter-based format
     */
    suspend fun searchAlbums(query: String, limit: Int = Constants.DEFAULT_SEARCH_LIMIT): List<Album> {
        return try {
            val response = apiClient.searchAlbums(
                query = query,
                limit = limit,
                include = "albums"
            )
            response.data.filterIsInstance<Album>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Probe album for Hi-Res capability with 429 retry handling
     * Returns album details with quality information
     */
    suspend fun probeAlbumQuality(albumId: String, maxRetries: Int = 3): Album? {
        var retries = 0
        val retryDelayMs = 1000L

        while (retries < maxRetries) {
            try {
                val details = apiClient.getAlbumDetails(albumId)
                return Album(
                    id = details.id,
                    title = details.title,
                    artist = details.artist,
                    coverUrl = details.cover,
                    releaseDate = details.releaseDate,
                    numberOfTracks = details.numberOfTracks,
                    audioQuality = details.audioQuality,
                    audioModes = details.audioModes,
                    isHiRes = details.isHiRes,
                    maxBitDepth = details.maxBitDepth,
                    maxSampleRate = details.maxSampleRate
                )
            } catch (e: HttpException) {
                when (e.code()) {
                    429 -> {
                        // Rate limited - wait and retry
                        retries++
                        if (retries < maxRetries) {
                            delay(retryDelayMs * retries) // Exponential backoff
                        }
                    }
                    else -> return null
                }
            } catch (e: Exception) {
                return null
            }
        }
        return null
    }

    /**
     * Get tracks from album with Hi-Res quality info
     */
    suspend fun getTracksFromAlbum(albumId: String): List<Track> {
        return try {
            val response = apiClient.getTracksFromAlbum(albumId)
            response.items
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Get albums by artist
     */
    suspend fun getAlbumsByArtist(artistId: String): List<Album> {
        return try {
            val response = apiClient.getAlbumsByArtist(artistId)
            response.items
        } catch (e: Exception) {
            emptyList()
        }
    }
}
