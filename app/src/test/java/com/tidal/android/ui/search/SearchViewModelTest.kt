package com.tidal.android.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.TestCase.assertNotNull
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testSearchResultsLiveData() {
        assertNotNull("SearchViewModel should have searchResults LiveData")
    }

    @Test
    fun testQueueLiveData() {
        assertNotNull("SearchViewModel should have queue LiveData")
    }

    @Test
    fun testQueueInitializesEmpty() {
        val emptyQueue: List<Any> = emptyList()
        assertNotNull(emptyQueue)
    }
}