package com.tidal.android.service.interceptor

import android.util.Log
import com.tidal.android.util.NetworkException
import okhttp3.Interceptor
import okhttp3.Response

class ErrorHandlingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        return when {
            response.code == 401 -> {
                Log.e("HTTP", "Unauthorized: ${response.message}")
                throw NetworkException("Unauthorized: Invalid or expired token")
            }
            response.code == 403 -> {
                Log.e("HTTP", "Forbidden: ${response.message}")
                throw NetworkException("Forbidden: Access denied")
            }
            response.code == 404 -> {
                Log.e("HTTP", "Not Found: ${response.message}")
                throw NetworkException("Not Found: Resource not available")
            }
            response.code in 500..599 -> {
                Log.e("HTTP", "Server Error: ${response.code} ${response.message}")
                throw NetworkException("Server Error: ${response.code}")
            }
            !response.isSuccessful -> {
                Log.e("HTTP", "Request failed: ${response.code} ${response.message}")
                throw NetworkException("Request failed: ${response.code}")
            }
            else -> response
        }
    }
}