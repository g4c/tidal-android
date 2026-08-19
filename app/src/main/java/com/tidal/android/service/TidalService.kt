package com.tidal.android.service

import com.tidal.android.model.Album
import com.tidal.android.model.Artist
import com.tidal.android.model.Track

interface TidalService {
    suspend fun authenticate(username: String, password: String): Boolean
    suspend fun searchArtists(query: String): List<Artist>
    suspend fun searchAlbums(query: String): List<Album>
    suspend fun searchTracks(query: String): List<Track>
    suspend fun getAlbumsByArtist(artistId: String): List<Album>
    suspend fun getTracksFromAlbum(albumId: String): List<Track>
    suspend fun getTrackStreamUrl(trackId: String): String
    suspend fun logout(): Boolean
}