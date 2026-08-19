package com.tidal.android.service.api

import com.tidal.android.model.*
import retrofit2.http.*

interface TidalApiClient {

    // Search endpoints - NEW API v2 format with filter-based queries
    @GET("searchResults")
    suspend fun searchTracks(
        @Query("filter[query]") query: String,
        @Query("limit") limit: Int = 20,
        @Query("include") include: String = "tracks,artists,albums"
    ): SearchResponse<Track>

    @GET("searchResults")
    suspend fun searchArtists(
        @Query("filter[query]") query: String,
        @Query("limit") limit: Int = 20,
        @Query("include") include: String = "artists"
    ): SearchResponse<Artist>

    @GET("searchResults")
    suspend fun searchAlbums(
        @Query("filter[query]") query: String,
        @Query("limit") limit: Int = 20,
        @Query("include") include: String = "albums"
    ): SearchResponse<Album>

    // Artist endpoints
    @GET("artists/{artistId}/albums")
    suspend fun getAlbumsByArtist(
        @Path("artistId") artistId: String
    ): AlbumResponse

    // Album endpoints
    @GET("albums/{albumId}/tracks")
    suspend fun getTracksFromAlbum(
        @Path("albumId") albumId: String
    ): TrackResponse

    // Get album details with quality info
    @GET("albums/{albumId}")
    suspend fun getAlbumDetails(
        @Path("albumId") albumId: String
    ): AlbumDetailsResponse

    // Stream URL endpoint
    @GET("tracks/{trackId}/streamUrl")
    suspend fun getTrackStreamUrl(
        @Path("trackId") trackId: String,
        @Query("quality") quality: String = "HIGH"
    ): StreamUrlResponse

    // Quality-aware stream endpoint for Hi-Res
    @GET("tracks/{trackId}/streamUrl")
    suspend fun getTrackStreamUrlWithQuality(
        @Path("trackId") trackId: String,
        @Query("quality") quality: String
    ): StreamUrlResponse
}

// Response models for JSON:API format
data class SearchResponse<T>(
    val data: List<T>,
    val included: List<Any>? = null
)

data class AlbumResponse(
    val items: List<Album>
)

data class TrackResponse(
    val items: List<Track>
)

data class StreamUrlResponse(
    val urls: List<String>,
    val quality: String? = null,
    val manifestMimeType: String? = null
)

data class AlbumDetailsResponse(
    val id: String,
    val title: String,
    val artist: Artist,
    val audioQuality: String? = null,
    val audioModes: List<String>? = null,
    val premiumStreamingOnly: Boolean = false,
    val numberOfTracks: Int,
    val duration: Int,
    val releaseDate: String? = null,
    val cover: String? = null,
    val numberOfVolumes: Int = 1,
    val isHiRes: Boolean = false,
    val maxBitDepth: Int? = null,
    val maxSampleRate: Int? = null
)
