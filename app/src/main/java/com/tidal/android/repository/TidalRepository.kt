package com.tidal.android.repository

import com.tidal.android.model.Album
import com.tidal.android.model.Artist
import com.tidal.android.model.Track
import com.tidal.android.service.TidalService

class TidalRepository(private val tidalService: TidalService) {

    suspend fun searchArtists(query: String): List<Artist> {
        return tidalService.searchArtists(query)
    }

    suspend fun searchAlbums(query: String): List<Album> {
        return tidalService.searchAlbums(query)
    }

    suspend fun searchTracks(query: String): List<Track> {
        return tidalService.searchTracks(query)
    }

    suspend fun getAlbumsByArtist(artistId: String): List<Album> {
        return tidalService.getAlbumsByArtist(artistId)
    }

    suspend fun getTracksFromAlbum(albumId: String): List<Track> {
        return tidalService.getTracksFromAlbum(albumId)
    }

    suspend fun getTrackStreamUrl(trackId: String): String {
        return tidalService.getTrackStreamUrl(trackId)
    }

    suspend fun logout(): Boolean {
        return tidalService.logout()
    }
}