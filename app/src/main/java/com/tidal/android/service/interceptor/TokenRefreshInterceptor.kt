package com.tidal.android.service.interceptor

import com.tidal.android.service.auth.OAuth2Manager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class TokenRefreshInterceptor(private val oauth2Manager: OAuth2Manager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Get access token (will refresh if needed)
        val accessToken = runBlocking {
            oauth2Manager.getAccessToken()
        }

        if (accessToken == null) {
            // No token available, return original response
            return chain.proceed(originalRequest)
        }

        // Add authorization header
        val requestWithAuth = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .header("X-Tidal-Token", accessToken)
            .build()

        var response = chain.proceed(requestWithAuth)

        // If 401, try to refresh token and retry
        if (response.code == 401) {
            val newToken = runBlocking {
                oauth2Manager.getAccessToken()
            }

            if (newToken != null && newToken != accessToken) {
                val retryRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $newToken")
                    .header("X-Tidal-Token", newToken)
                    .build()

                response.close()
                response = chain.proceed(retryRequest)
            }
        }

        return response
    }
}