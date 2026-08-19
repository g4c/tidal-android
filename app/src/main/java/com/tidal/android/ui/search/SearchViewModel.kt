package com.tidal.android.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tidal.android.model.Album
import com.tidal.android.model.Artist
import com.tidal.android.model.Track
import com.tidal.android.repository.TidalRepository
import com.tidal.android.service.impl.TidalServiceImpl
import com.tidal.android.util.Result
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: TidalRepository) : ViewModel() {

    private val _searchResults = MutableLiveData<Result<List<Any>>>()
    val searchResults: LiveData<Result<List<Any>>> = _searchResults

    private val _queue = MutableLiveData<List<Track>>(arrayListOf())
    val queue: LiveData<List<Track>> = _queue

    fun searchArtists(query: String) {
        viewModelScope.launch {
            _searchResults.value = Result.Loading
            val results = repository.searchArtists(query)
            _searchResults.value = Result.Success(results as List<Any>)
        }
    }

    fun searchAlbums(query: String) {
        viewModelScope.launch {
            _searchResults.value = Result.Loading
            val results = repository.searchAlbums(query)
            _searchResults.value = Result.Success(results as List<Any>)
        }
    }

    fun searchTracks(query: String) {
        viewModelScope.launch {
            _searchResults.value = Result.Loading
            val results = repository.searchTracks(query)
            _searchResults.value = Result.Success(results as List<Any>)
        }
    }

    fun addToQueue(track: Track) {
        val currentQueue = (_queue.value ?: emptyList()).toMutableList()
        currentQueue.add(track)
        _queue.value = currentQueue
    }

    fun removeFromQueue(track: Track) {
        val currentQueue = (_queue.value ?: emptyList()).toMutableList()
        currentQueue.remove(track)
        _queue.value = currentQueue
    }

    fun clearQueue() {
        _queue.value = emptyList()
    }
}

class SearchViewModelFactory(private val repository: TidalRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}