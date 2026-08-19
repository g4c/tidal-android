package com.tidal.android.util

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()

    suspend inline fun <R> map(transform: suspend (T) -> R): Result<R> {
        return when (this) {
            is Success -> Result.Success(transform(data))
            is Error -> Result.Error(exception)
            is Loading -> Result.Loading
        }
    }
}