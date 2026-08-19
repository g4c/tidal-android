package com.tidal.android.service.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class LoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val startTime = System.nanoTime()
        Log.d(
            TAG, String.format(
                "Sending request %s on %s%n%s",
                request.url, chain.connection(), request.headers
            )
        )

        val response = chain.proceed(request)
        val elapsedTime = (System.nanoTime() - startTime) / 1_000_000.0

        Log.d(
            TAG, String.format(
                "Received response for %s in %.1fms%n%s",
                response.request.url, elapsedTime, response.headers
            )
        )

        return response
    }

    companion object {
        private const val TAG = "LoggingInterceptor"
    }
}