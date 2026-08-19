package com.tidal.android.util

object Constants {
    // Tidal API Configuration
    const val TIDAL_BASE_URL = "https://api.tidal.com/v1/"
    const val TIDAL_CLIENT_ID = "YOUR_CLIENT_ID_HERE"
    const val TIDAL_CLIENT_SECRET = "YOUR_CLIENT_SECRET_HERE"

    // OAuth2 Configuration
    const val OAUTH_REDIRECT_URL = "com.tidal.android://oauth-callback"
    const val TOKEN_EXPIRY_BUFFER_SECONDS = 60
    const val TOKEN_REFRESH_INTERVAL_MINUTES = 50

    // Download Configuration
    const val DOWNLOAD_FOLDER = "Tidal Music"
    const val MAX_CONCURRENT_DOWNLOADS = 3
    const val DOWNLOAD_TIMEOUT_SECONDS = 300
    const val CHUNK_SIZE = 8192 // 8KB chunks

    // Network Configuration
    const val CONNECT_TIMEOUT_SECONDS = 30L
    const val READ_TIMEOUT_SECONDS = 30L
    const val WRITE_TIMEOUT_SECONDS = 30L

    // Search Configuration
    const val DEFAULT_SEARCH_LIMIT = 20
    const val MAX_SEARCH_RESULTS = 100

    // API Endpoints
    const val ENDPOINT_SEARCH = "search/"
    const val ENDPOINT_ARTISTS = "artists/"
    const val ENDPOINT_ALBUMS = "albums/"
    const val ENDPOINT_TRACKS = "tracks/"
    const val ENDPOINT_STREAM = "/streamUrl"

    // Error Messages
    const val ERROR_NO_INTERNET = "No internet connection"
    const val ERROR_AUTHENTICATION = "Authentication failed"
    const val ERROR_SEARCH_FAILED = "Search failed"
    const val ERROR_DOWNLOAD_FAILED = "Download failed"
    const val ERROR_PERMISSION_DENIED = "Permission denied"

    // Preferences
    const val PREF_NAME = "tidal_preferences"
    const val PREF_TOKEN = "access_token"
    const val PREF_REFRESH_TOKEN = "refresh_token"
    const val PREF_TOKEN_EXPIRY = "token_expiry"
    const val PREF_USER_ID = "user_id"
    const val PREF_USERNAME = "username"
}
