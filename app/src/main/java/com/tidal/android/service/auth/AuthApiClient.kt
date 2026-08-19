package com.tidal.android.service.auth

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApiClient {

    @FormUrlEncoded
    @POST("oauth2/token")
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String = "client_credentials"
    ): AuthResponse

    @FormUrlEncoded
    @POST("oauth2/token")
    suspend fun refreshAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("refresh_token") refreshToken: String
    ): AuthResponse
}

data class AuthResponse(
    val sessionId: String? = null,
    val userId: Int? = null,
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val access_token: String,
    val refresh_token: String? = null,
    val expires_in: Int,
    val token_type: String = "Bearer",
    val scope: String? = null
) {
    // Compatibility properties
    val accessToken: String get() = access_token
    val expiresIn: Int get() = expires_in
    val refreshToken: String? get() = refresh_token
}