package com.tidal.android.service.api

import com.tidal.android.model.Album
import com.tidal.android.model.Artist
import com.tidal.android.model.Track
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TidalApiClient {

    @GET("search/artists")
    suspend fun searchArtists(
        @Query("query") query: String,
        @Query("limit") limit: Int = 20
    ): SearchResponse<Artist>

    @GET("search/albums")
    suspend fun searchAlbums(
        @Query("query") query: String,
        @Query("limit") limit: Int = 20
    ): SearchResponse<Album>

    @GET("search/tracks")
    suspend fun searchTracks(
        @Query("query") query: String,
        @Query("limit") limit: Int = 20
    ): SearchResponse<Track>

    @GET("artists/{id}/albums")
    suspend fun getAlbumsByArtist(
        @Path("id") artistId: String,
        @Query("limit") limit: Int = 50
    ): SearchResponse<Album>

    @GET("albums/{id}/tracks")
    suspend fun getTracksFromAlbum(
        @Path("id") albumId: String,
        @Query("limit") limit: Int = 100
    ): SearchResponse<Track>

    @GET("tracks/{id}/streamUrl")
    suspend fun getTrackStreamUrl(
        @Path("id") trackId: String
    ): StreamUrlResponse
}

data class SearchResponse<T>(
    val items: List<T> = emptyList(),
    val totalNumberOfItems: Int = 0,
    val limit: Int = 20,
    val offset: Int = 0
)

data class StreamUrlResponse(
    val trackId: String,
    val soundQuality: String = "HIGH",
    val urls: List<String> = emptyList()
)