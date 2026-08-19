package com.tidal.android.service.auth

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test

class OAuth2ManagerTest {

    @Test
    fun testAuthResponseParsing() {
        val response = AuthResponse(
            access_token = "test_token_123",
            expires_in = 3600,
            token_type = "Bearer"
        )

        assertEquals("test_token_123", response.access_token)
        assertEquals("test_token_123", response.accessToken)
        assertEquals(3600, response.expires_in)
        assertEquals(3600, response.expiresIn)
        assertEquals("Bearer", response.token_type)
    }

    @Test
    fun testRefreshTokenOptional() {
        val responseWithoutRefresh = AuthResponse(
            access_token = "token_123",
            expires_in = 3600
        )

        assertEquals("token_123", responseWithoutRefresh.access_token)
        assertEquals(null, responseWithoutRefresh.refresh_token)
    }

    @Test
    fun testRefreshTokenIncluded() {
        val responseWithRefresh = AuthResponse(
            access_token = "token_123",
            refresh_token = "refresh_123",
            expires_in = 3600
        )

        assertEquals("token_123", responseWithRefresh.access_token)
        assertNotNull(responseWithRefresh.refresh_token)
        assertEquals("refresh_123", responseWithRefresh.refresh_token)
    }
}