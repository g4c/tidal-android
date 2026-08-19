package com.tidal.android.service.auth

data class AuthRequest(
    val username: String,
    val password: String,
    val token: String,
    val clientVersion: String = "2.26.1"
)

data class AuthResponse(
    val sessionId: String,
    val userId: Int,
    val email: String,
    val firstName: String,
    val lastName: String,
    val accessToken: String,
    val refreshToken: String?,
    val expiresIn: Int
)

data class TokenRefreshRequest(
    val refreshToken: String
)

data class TokenRefreshResponse(
    val accessToken: String,
    val refreshToken: String?,
    val expiresIn: Int
)