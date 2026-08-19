package com.tidal.android.service.impl

import android.content.Context
import com.google.gson.Gson
import com.tidal.android.model.Album
import com.tidal.android.model.Artist
import com.tidal.android.model.Track
import com.tidal.android.service.TidalService
import com.tidal.android.service.api.TidalApiClient
import com.tidal.android.service.auth.AuthRequest
import com.tidal.android.service.auth.TokenManager
import com.tidal.android.service.interceptor.AuthInterceptor
import com.tidal.android.service.interceptor.ErrorHandlingInterceptor
import com.tidal.android.service.interceptor.LoggingInterceptor
import com.tidal.android.util.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class TidalServiceImpl(context: Context) : TidalService {

    private val tokenManager = TokenManager(context)
    private val apiClient: TidalApiClient

    init {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(LoggingInterceptor())
            .addInterceptor(AuthInterceptor(tokenManager))
            .addInterceptor(ErrorHandlingInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.TIDAL_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()

        apiClient = retrofit.create(TidalApiClient::class.java)
    }

    override suspend fun authenticate(username: String, password: String): Boolean {
        return try {
            // In real implementation, call actual auth endpoint
            // val response = apiClient.authenticate(AuthRequest(username, password, TOKEN))
            // tokenManager.saveAccessToken(response.accessToken)
            // tokenManager.saveUserId(response.userId)
            // return true

            // Mock implementation
            tokenManager.saveAccessToken("mock_token_${System.currentTimeMillis()}")
            tokenManager.saveUserId(123)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun searchArtists(query: String): List<Artist> {
        return try {
            val response = apiClient.searchArtists(query)
            response.items
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun searchAlbums(query: String): List<Album> {
        return try {
            val response = apiClient.searchAlbums(query)
            response.items
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun searchTracks(query: String): List<Track> {
        return try {
            val response = apiClient.searchTracks(query)
            response.items
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getAlbumsByArtist(artistId: String): List<Album> {
        return try {
            val response = apiClient.getAlbumsByArtist(artistId)
            response.items
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getTracksFromAlbum(albumId: String): List<Track> {
        return try {
            val response = apiClient.getTracksFromAlbum(albumId)
            response.items
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getTrackStreamUrl(trackId: String): String {
        return try {
            val response = apiClient.getTrackStreamUrl(trackId)
            response.urls.firstOrNull() ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    override suspend fun logout(): Boolean {
        return try {
            tokenManager.clearTokens()
            true
        } catch (e: Exception) {
            false
        }
    }
}