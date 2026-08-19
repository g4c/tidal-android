package com.tidal.android.service

import com.tidal.android.model.Album
import com.tidal.android.model.Artist
import com.tidal.android.model.Track
import kotlinx.coroutines.delay

class MockTidalService : TidalService {
    private var authenticated = false
    private val mockArtists: List<Artist>
    private val mockAlbums: List<Album>
    private val mockTracks: List<Track>

    init {
        initializeMockData()
    }

    private fun initializeMockData() {
        val artist1 = Artist("1", "The Beatles")
        val artist2 = Artist("2", "Pink Floyd")
        val artist3 = Artist("3", "David Bowie")
        mockArtists = listOf(artist1, artist2, artist3)

        val album1 = Album("1", "Abbey Road", listOf(artist1), "1969-09-26", "cover1.jpg")
        val album2 = Album("2", "The Wall", listOf(artist2), "1979-11-30", "cover2.jpg")
        val album3 = Album("3", "Ziggy Stardust", listOf(artist3), "1972-06-16", "cover3.jpg")
        val album4 = Album("4", "Let It Be", listOf(artist1), "1970-05-08", "cover4.jpg")
        mockAlbums = listOf(album1, album2, album3, album4)

        mockTracks = listOf(
            Track("1", "Come Together", artist1, album1, 1, 259, "http://mock.tidal.com/track1.flac"),
            Track("2", "Something", artist1, album1, 2, 183, "http://mock.tidal.com/track2.flac"),
            Track("3", "Maxwell's Silver Hammer", artist1, album1, 3, 207, "http://mock.tidal.com/track3.flac"),
            Track("4", "In The Flesh?", artist2, album2, 1, 363, "http://mock.tidal.com/track4.flac"),
            Track("5", "The Thin Ice", artist2, album2, 2, 289, "http://mock.tidal.com/track5.flac"),
            Track("6", "The Happiest Days", artist2, album2, 3, 217, "http://mock.tidal.com/track6.flac"),
            Track("7", "Five Years", artist3, album3, 1, 370, "http://mock.tidal.com/track7.flac"),
            Track("8", "Soul Love", artist3, album3, 2, 236, "http://mock.tidal.com/track8.flac"),
            Track("9", "Two of Us", artist1, album4, 1, 137, "http://mock.tidal.com/track9.flac"),
            Track("10", "Dig a Pony", artist1, album4, 2, 184, "http://mock.tidal.com/track10.flac")
        )
    }

    override suspend fun authenticate(username: String, password: String): Boolean {
        delay(500) // Simulate network delay
        authenticated = true
        return true
    }

    override suspend fun searchArtists(query: String): List<Artist> {
        delay(300)
        return mockArtists.filter { it.name.contains(query, ignoreCase = true) }
    }

    override suspend fun searchAlbums(query: String): List<Album> {
        delay(300)
        return mockAlbums.filter { it.title.contains(query, ignoreCase = true) }
    }

    override suspend fun searchTracks(query: String): List<Track> {
        delay(300)
        return mockTracks.filter { it.title.contains(query, ignoreCase = true) }
    }

    override suspend fun getAlbumsByArtist(artistId: String): List<Album> {
        delay(300)
        return mockAlbums.filter { album -> album.artists.any { it.id == artistId } }
    }

    override suspend fun getTracksFromAlbum(albumId: String): List<Track> {
        delay(300)
        return mockTracks.filter { it.album.id == albumId }
    }

    override suspend fun getDownloadUrl(trackId: String): String {
        delay(200)
        return mockTracks.find { it.id == trackId }?.url ?: ""
    }

    override suspend fun getTrackMetadata(trackId: String): Track {
        delay(200)
        return mockTracks.find { it.id == trackId } ?: throw Exception("Track not found")
    }
}