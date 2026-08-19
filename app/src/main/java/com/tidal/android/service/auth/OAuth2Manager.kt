package com.tidal.android.service.auth

import android.content.Context
import com.tidal.android.util.Constants
import com.tidal.android.util.NetworkException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.locks.ReentrantReadWriteLock

class OAuth2Manager(context: Context) {

    private val tokenManager = TokenManager(context)
    private val authApiClient: AuthApiClient
    private val lock = ReentrantReadWriteLock()
    private var isRefreshing = false

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.TIDAL_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        authApiClient = retrofit.create(AuthApiClient::class.java)
    }

    /**
     * Get access token, refreshing if necessary
     */
    suspend fun getAccessToken(): String? {
        lock.readLock().lock()
        try {
            if (!tokenManager.isTokenExpired()) {
                return tokenManager.getAccessToken()
            }
        } finally {
            lock.readLock().unlock()
        }

        // Token expired, need to refresh
        return refreshAccessToken()
    }

    /**
     * Acquire new access token using Client Credentials flow
     */
    suspend fun acquireAccessToken(): Boolean {
        lock.writeLock().lock()
        try {
            return try {
                val response = authApiClient.getAccessToken(
                    clientId = Constants.TIDAL_CLIENT_ID,
                    clientSecret = Constants.TIDAL_CLIENT_SECRET,
                    grantType = "client_credentials"
                )

                tokenManager.saveAccessToken(response.access_token)
                tokenManager.saveTokenExpiresIn(response.expires_in)

                if (!response.refresh_token.isNullOrEmpty()) {
                    tokenManager.saveRefreshToken(response.refresh_token)
                }

                true
            } catch (e: Exception) {
                false
            }
        } finally {
            lock.writeLock().unlock()
        }
    }

    /**
     * Refresh access token using refresh token
     */
    private suspend fun refreshAccessToken(): String? {
        lock.writeLock().lock()
        try {
            if (isRefreshing) {
                // Already refreshing, wait and return current token
                return tokenManager.getAccessToken()
            }

            isRefreshing = true
        } finally {
            lock.writeLock().unlock()
        }

        return try {
            val refreshToken = tokenManager.getRefreshToken()
            if (refreshToken != null) {
                val response = authApiClient.refreshAccessToken(
                    clientId = Constants.TIDAL_CLIENT_ID,
                    clientSecret = Constants.TIDAL_CLIENT_SECRET,
                    grantType = "refresh_token",
                    refreshToken = refreshToken
                )

                tokenManager.saveAccessToken(response.access_token)
                tokenManager.saveTokenExpiresIn(response.expires_in)

                if (!response.refresh_token.isNullOrEmpty()) {
                    tokenManager.saveRefreshToken(response.refresh_token)
                }

                response.access_token
            } else {
                // No refresh token, try to acquire new one
                if (acquireAccessToken()) {
                    tokenManager.getAccessToken()
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            // If refresh fails, try to acquire new token
            if (acquireAccessToken()) {
                tokenManager.getAccessToken()
            } else {
                null
            }
        } finally {
            lock.writeLock().lock()
            try {
                isRefreshing = false
            } finally {
                lock.writeLock().unlock()
            }
        }
    }

    /**
     * Check if we have valid token
     */
    fun hasValidToken(): Boolean = tokenManager.hasValidToken()

    /**
     * Clear tokens on logout
     */
    fun logout() {
        lock.writeLock().lock()
        try {
            tokenManager.clearTokens()
        } finally {
            lock.writeLock().unlock()
        }
    }
}