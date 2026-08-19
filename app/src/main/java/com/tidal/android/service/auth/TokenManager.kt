package com.tidal.android.service.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class TokenManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "tidal_auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveAccessToken(token: String) {
        sharedPreferences.edit().putString(KEY_ACCESS_TOKEN, token).apply()
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
    }

    fun saveRefreshToken(token: String) {
        sharedPreferences.edit().putString(KEY_REFRESH_TOKEN, token).apply()
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
    }

    fun saveTokenExpiresIn(expiresIn: Int) {
        val expiresAt = System.currentTimeMillis() + (expiresIn * 1000)
        sharedPreferences.edit().putLong(KEY_TOKEN_EXPIRES_AT, expiresAt).apply()
    }

    fun isTokenExpired(): Boolean {
        val expiresAt = sharedPreferences.getLong(KEY_TOKEN_EXPIRES_AT, 0L)
        return System.currentTimeMillis() > expiresAt
    }

    fun saveUserId(userId: Int) {
        sharedPreferences.edit().putInt(KEY_USER_ID, userId).apply()
    }

    fun getUserId(): Int {
        return sharedPreferences.getInt(KEY_USER_ID, -1)
    }

    fun clearTokens() {
        sharedPreferences.edit().apply {
            remove(KEY_ACCESS_TOKEN)
            remove(KEY_REFRESH_TOKEN)
            remove(KEY_TOKEN_EXPIRES_AT)
            remove(KEY_USER_ID)
        }.apply()
    }

    fun isLoggedIn(): Boolean {
        return getAccessToken() != null && getUserId() != -1
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_TOKEN_EXPIRES_AT = "token_expires_at"
        private const val KEY_USER_ID = "user_id"
    }
}