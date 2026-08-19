package com.tidal.android.repository

import com.tidal.android.model.Album
import com.tidal.android.model.Artist
import com.tidal.android.model.Track
import com.tidal.android.service.TidalService
import com.tidal.android.util.Result

class TidalRepository(private val tidalService: TidalService) {

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

    suspend fun searchTracks(query: String): Result<List<Track>> {
        return try {
            val tracks = tidalService.searchTracks(query)
            Result.Success(tracks)
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

    suspend fun authenticate(username: String, password: String): Result<Boolean> {
        return try {
            val result = tidalService.authenticate(username, password)
            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(e as Exception)
        }
    }
}