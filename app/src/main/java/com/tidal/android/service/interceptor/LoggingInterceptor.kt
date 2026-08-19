package com.tidal.android.service.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startTime = System.nanoTime()

        Log.d(
            "HTTP",
            "Sending request: ${request.url}\nHeaders: ${request.headers}"
        )

        val response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            Log.e("HTTP", "Request failed: ${e.message}", e)
            throw e
        }

        val elapsedTime = (System.nanoTime() - startTime) / 1_000_000.0
        Log.d(
            "HTTP",
            "Response: ${response.code}\nTime: ${elapsedTime}ms\n" +
                "Body: ${response.body?.string()}"
        )

        return response
    }
}