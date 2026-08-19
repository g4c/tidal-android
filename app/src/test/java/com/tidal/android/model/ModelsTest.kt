package com.tidal.android.model

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test

class ModelsTest {

    @Test
    fun testArtistCreation() {
        val artist = Artist(
            id = "artist_1",
            name = "Test Artist",
            picture = "http://example.com/image.jpg",
            popularity = 85
        )

        assertEquals("artist_1", artist.id)
        assertEquals("Test Artist", artist.name)
        assertEquals(85, artist.popularity)
        assertEquals(false, artist.verified)
    }

    @Test
    fun testAlbumCreation() {
        val album = Album(
            id = "album_1",
            title = "Test Album",
            numberOfTracks = 10,
            duration = 2400,
            releaseDate = "2024-01-01"
        )

        assertEquals("album_1", album.id)
        assertEquals("Test Album", album.title)
        assertEquals(10, album.numberOfTracks)
        assertEquals(2400, album.duration)
    }

    @Test
    fun testTrackCreation() {
        val track = Track(
            id = "track_1",
            title = "Test Track",
            duration = 240,
            trackNumber = 1
        )

        assertEquals("track_1", track.id)
        assertEquals("Test Track", track.title)
        assertEquals(240, track.duration)
        assertEquals(1, track.trackNumber)
        assertEquals(false, track.explicit)
    }

    @Test
    fun testTrackWithArtistAndAlbum() {
        val artist = Artist(id = "artist_1", name = "Test Artist")
        val album = Album(id = "album_1", title = "Test Album")
        val track = Track(
            id = "track_1",
            title = "Test Track",
            artist = artist,
            album = album,
            duration = 240
        )

        assertNotNull(track.artist)
        assertEquals("Test Artist", track.artist?.name)
        assertNotNull(track.album)
        assertEquals("Test Album", track.album?.title)
    }
}