package com.tidal.android.service.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.util.concurrent.locks.ReentrantReadWriteLock

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

    private val lock = ReentrantReadWriteLock()

    fun saveAccessToken(token: String) {
        lock.writeLock().lock()
        try {
            sharedPreferences.edit().putString(KEY_ACCESS_TOKEN, token).apply()
        } finally {
            lock.writeLock().unlock()
        }
    }

    fun getAccessToken(): String? {
        lock.readLock().lock()
        try {
            return sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
        } finally {
            lock.readLock().unlock()
        }
    }

    fun saveRefreshToken(token: String) {
        lock.writeLock().lock()
        try {
            sharedPreferences.edit().putString(KEY_REFRESH_TOKEN, token).apply()
        } finally {
            lock.writeLock().unlock()
        }
    }

    fun getRefreshToken(): String? {
        lock.readLock().lock()
        try {
            return sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
        } finally {
            lock.readLock().unlock()
        }
    }

    fun saveTokenExpiresIn(expiresIn: Int) {
        lock.writeLock().lock()
        try {
            // Add 60 second buffer to refresh before actual expiration
            val expiresAt = System.currentTimeMillis() + ((expiresIn - 60) * 1000)
            sharedPreferences.edit().putLong(KEY_TOKEN_EXPIRES_AT, expiresAt).apply()
        } finally {
            lock.writeLock().unlock()
        }
    }

    fun isTokenExpired(): Boolean {
        lock.readLock().lock()
        try {
            val expiresAt = sharedPreferences.getLong(KEY_TOKEN_EXPIRES_AT, 0L)
            return System.currentTimeMillis() > expiresAt
        } finally {
            lock.readLock().unlock()
        }
    }

    fun clearTokens() {
        lock.writeLock().lock()
        try {
            sharedPreferences.edit().apply {
                remove(KEY_ACCESS_TOKEN)
                remove(KEY_REFRESH_TOKEN)
                remove(KEY_TOKEN_EXPIRES_AT)
            }.apply()
        } finally {
            lock.writeLock().unlock()
        }
    }

    fun hasValidToken(): Boolean {
        lock.readLock().lock()
        try {
            return getAccessToken() != null && !isTokenExpired()
        } finally {
            lock.readLock().unlock()
        }
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_TOKEN_EXPIRES_AT = "token_expires_at"
    }
}