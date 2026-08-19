package com.tidal.android.util

import java.io.Serializable

sealed class Result<T> : Serializable {
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val exception: Exception) : Result<T>()
    class Loading<T> : Result<T>()
}
