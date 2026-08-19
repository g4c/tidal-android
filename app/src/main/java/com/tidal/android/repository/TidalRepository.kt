package com.tidal.android.repository

import com.tidal.android.model.Album
import com.tidal.android.model.Artist
import com.tidal.android.model.Track
import com.tidal.android.service.TidalService
import com.tidal.android.service.impl.SearchServiceImpl
import com.tidal.android.util.Result

class TidalRepository(private val tidalService: TidalService) {

    private lateinit var searchService: SearchServiceImpl

    /**
     * Search tracks using new TIDAL API v2 format
     * /searchResults?filter[query]={query}&include=tracks,artists,albums
     */
    suspend fun searchTracks(query: String): Result<List<Track>> {
        return try {
            val tracks = tidalService.searchTracks(query)
            Result.Success(tracks)
        } catch (e: Exception) {
            Result.Error(e as Exception)
        }
    }

    suspend fun searchArtists(query: String): Result<List<Artist>> {
        return try {
            val artists = tidalService.searchArtists(query)
            Result.Success(artists)
        } catch (e: Exception) {
            Result.Error(e as Exception)
        }
    }

    suspend fun searchAlbums(query: String): Result<List<Album>> {
        return try {
            val albums = tidalService.searchAlbums(query)
            Result.Success(albums)
        } catch (e: Exception) {
            Result.Error(e as Exception)
        }
    }

    suspend fun getAlbumsByArtist(artistId: String): Result<List<Album>> {
        return try {
            val albums = tidalService.getAlbumsByArtist(artistId)
            Result.Success(albums)
        } catch (e: Exception) {
            Result.Error(e as Exception)
        }
    }

    suspend fun getTracksFromAlbum(albumId: String): Result<List<Track>> {
        return try {
            val tracks = tidalService.getTracksFromAlbum(albumId)
            Result.Success(tracks)
        } catch (e: Exception) {
            Result.Error(e as Exception)
        }
    }

    /**
     * Probe album for Hi-Res quality info with 429 retry handling
     */
    suspend fun probeAlbumQuality(albumId: String): Result<Album?> {
        return try {
            val album = tidalService.probeAlbumQuality(albumId)
            Result.Success(album)
        } catch (e: Exception) {
            Result.Error(e as Exception)
        }
    }

    suspend fun getTrackStreamUrl(trackId: String): Result<String> {
        return try {
            val url = tidalService.getTrackStreamUrl(trackId)
            Result.Success(url)
        } catch (e: Exception) {
            Result.Error(e as Exception)
        }
    }
}
