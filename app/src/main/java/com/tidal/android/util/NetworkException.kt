package com.tidal.android.util

sealed class NetworkException(message: String, cause: Throwable? = null) :
    Exception(message, cause) {

    class UnauthorizedException(message: String = "Unauthorized") : NetworkException(message)
    class ForbiddenException(message: String = "Forbidden") : NetworkException(message)
    class NotFoundException(message: String = "Not found") : NetworkException(message)
    class RateLimitException(message: String = "Rate limit exceeded") : NetworkException(message)
    class ServerException(message: String = "Server error") : NetworkException(message)
    class HttpException(val code: Int, message: String = "HTTP error $code") :
        NetworkException(message)

    class ConnectionException(message: String = "Connection error", cause: Throwable? = null) :
        NetworkException(message, cause)

    class TimeoutException(message: String = "Request timeout", cause: Throwable? = null) :
        NetworkException(message, cause)
}