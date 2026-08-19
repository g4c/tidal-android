package com.tidal.android.service.interceptor

import com.tidal.android.service.auth.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Add authorization header if token exists
        val token = tokenManager.getAccessToken()
        val request = if (token != null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .header("X-Tidal-Token", token)
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(request)
    }
}