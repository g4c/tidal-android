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
        @Query("q") query: String,
        @Query("limit") limit: Int = 20
    ): SearchResponse<Artist>

    @GET("search/albums")
    suspend fun searchAlbums(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20
    ): SearchResponse<Album>

    @GET("search/tracks")
    suspend fun searchTracks(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20
    ): SearchResponse<Track>

    @GET("artists/{artistId}/albums")
    suspend fun getAlbumsByArtist(
        @Path("artistId") artistId: String,
        @Query("limit") limit: Int = 50
    ): AlbumsResponse

    @GET("albums/{albumId}/tracks")
    suspend fun getTracksFromAlbum(
        @Path("albumId") albumId: String,
        @Query("limit") limit: Int = 100
    ): TracksResponse

    @GET("tracks/{trackId}/streamUrl")
    suspend fun getTrackStreamUrl(
        @Path("trackId") trackId: String
    ): StreamUrlResponse
}

data class SearchResponse<T>(
    val items: List<T>,
    val total: Int,
    val offset: Int,
    val limit: Int
)

data class AlbumsResponse(
    val items: List<Album>,
    val total: Int
)

data class TracksResponse(
    val items: List<Track>,
    val total: Int
)

data class StreamUrlResponse(
    val trackId: String,
    val urls: List<String>,
    val codec: String,
    val mimeType: String
)