package com.tidal.android.util

seal class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val exception: Exception) : Result<T>()
    class Loading<T> : Result<T>()
}

class NetworkException(message: String, cause: Throwable? = null) :
    Exception(message, cause)

class AuthenticationException(message: String, cause: Throwable? = null) :
    Exception(message, cause)

class FileException(message: String, cause: Throwable? = null) :
    Exception(message, cause)