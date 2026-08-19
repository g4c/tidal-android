package com.tidal.android.service

import com.tidal.android.model.Album
import com.tidal.android.model.Artist
import com.tidal.android.model.Track

interface TidalService {
    @Throws(Exception::class)
    suspend fun authenticate(username: String, password: String): Boolean

    @Throws(Exception::class)
    suspend fun searchArtists(query: String): List<Artist>

    @Throws(Exception::class)
    suspend fun searchAlbums(query: String): List<Album>

    @Throws(Exception::class)
    suspend fun searchTracks(query: String): List<Track>

    @Throws(Exception::class)
    suspend fun getAlbumsByArtist(artistId: String): List<Album>

    @Throws(Exception::class)
    suspend fun getTracksFromAlbum(albumId: String): List<Track>

    @Throws(Exception::class)
    suspend fun getDownloadUrl(trackId: String): String

    @Throws(Exception::class)
    suspend fun getTrackMetadata(trackId: String): Track
}