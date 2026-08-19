package com.tidal.android.service.interceptor

import com.tidal.android.util.NetworkException
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject

class ErrorHandlingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (!response.isSuccessful) {
            val errorBody = response.body?.string() ?: ""
            val errorMessage = try {
                val json = JSONObject(errorBody)
                json.optString("userMessage", json.optString("message", "Unknown error"))
            } catch (e: Exception) {
                "Unknown error"
            }

            val exception = when (response.code) {
                401 -> NetworkException.UnauthorizedException(errorMessage)
                403 -> NetworkException.ForbiddenException(errorMessage)
                404 -> NetworkException.NotFoundException(errorMessage)
                429 -> NetworkException.RateLimitException(errorMessage)
                500, 502, 503 -> NetworkException.ServerException(errorMessage)
                else -> NetworkException.HttpException(response.code, errorMessage)
            }

            throw exception
        }

        return response
    }
}