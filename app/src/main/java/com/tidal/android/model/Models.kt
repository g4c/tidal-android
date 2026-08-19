package com.tidal.android.model

import java.io.Serializable

data class Artist(
    val id: String,
    val name: String,
    val picture: String? = null,
    val url: String? = null,
    val popularity: Int = 0,
    val verified: Boolean = false
) : Serializable

data class Album(
    val id: String,
    val title: String,
    val description: String? = null,
    val cover: String? = null,
    val artist: Artist? = null,
    val artistId: String? = null,
    val releaseDate: String? = null,
    val numberOfTracks: Int = 0,
    val duration: Int = 0,
    val popularity: Int = 0,
    val explicit: Boolean = false
) : Serializable

data class Track(
    val id: String,
    val title: String,
    val description: String? = null,
    val artist: Artist? = null,
    val artistId: String? = null,
    val album: Album? = null,
    val albumId: String? = null,
    val duration: Int = 0,
    val trackNumber: Int = 0,
    val popularity: Int = 0,
    val explicit: Boolean = false,
    val isrc: String? = null,
    val url: String? = null
) : Serializable