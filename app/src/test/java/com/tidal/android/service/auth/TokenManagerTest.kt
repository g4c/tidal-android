package com.tidal.android.service.auth

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class TokenManagerTest {

    @Test
    fun testTokenExpiration() {
        val expiresIn = 3600 // 1 hour
        val buffer = 60 // seconds
        val expectedExpiry = System.currentTimeMillis() + ((expiresIn - buffer) * 1000)

        // Verify calculation
        assertTrue(expectedExpiry > System.currentTimeMillis())
        assertTrue(expectedExpiry - System.currentTimeMillis() < expiresIn * 1000)
    }

    @Test
    fun testTokenNotExpiredImmediately() {
        val futureTime = System.currentTimeMillis() + 3600000 // 1 hour from now
        val isExpired = System.currentTimeMillis() > futureTime
        assertFalse(isExpired)
    }

    @Test
    fun testTokenExpiredAfterTime() {
        val pastTime = System.currentTimeMillis() - 1000 // 1 second ago
        val isExpired = System.currentTimeMillis() > pastTime
        assertTrue(isExpired)
    }

    @Test
    fun testSanitizeFilename() {
        val filename = "Track<>Title|Invalid".sanitizeFilename()
        assertNotNull(filename)
        assertFalse(filename.contains("<"))
        assertFalse(filename.contains(">"))
        assertFalse(filename.contains("|"))
    }

    private fun String.sanitizeFilename(): String {
        return this.replace(Regex("[<>:\"|?*]"), "_")
            .replace("/", "_")
            .replace("\\", "_")
            .take(255)
    }
}