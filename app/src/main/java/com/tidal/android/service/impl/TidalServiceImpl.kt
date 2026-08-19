package com.tidal.android.service.impl

import android.content.Context
import com.tidal.android.model.Album
import com.tidal.android.model.Artist
import com.tidal.android.model.Track
import com.tidal.android.service.TidalService
import com.tidal.android.service.api.TidalApiClient
import com.tidal.android.service.auth.OAuth2Manager
import com.tidal.android.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class TidalServiceImpl(private val context: Context) : TidalService {

    private val oauth2Manager = OAuth2Manager(context)
    private val apiClient: TidalApiClient

    init {
        apiClient = createRetrofitClient()
    }

    private fun createRetrofitClient(): TidalApiClient {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(Constants.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(Constants.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(Constants.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val token = oauth2Manager.getValidToken()
                val request = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .header("Accept", "application/json")
                    .build()
                chain.proceed(request)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.TIDAL_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TidalApiClient::class.java)
    }

    override suspend fun authenticate(username: String, password: String): Boolean {
        return oauth2Manager.authenticate(username, password)
    }

    override suspend fun searchArtists(query: String): List<Artist> {
        return try {
            val response = apiClient.searchArtists(query, Constants.DEFAULT_SEARCH_LIMIT)
            response.items
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun searchAlbums(query: String): List<Album> {
        return try {
            val response = apiClient.searchAlbums(query, Constants.DEFAULT_SEARCH_LIMIT)
            response.items
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun searchTracks(query: String): List<Track> {
        return try {
            val response = apiClient.searchTracks(query, Constants.DEFAULT_SEARCH_LIMIT)
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
        return oauth2Manager.logout()
    }
}
