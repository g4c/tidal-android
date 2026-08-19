package com.tidal.android.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tidal.android.model.Album
import com.tidal.android.model.Artist
import com.tidal.android.model.Track
import com.tidal.android.repository.TidalRepository
import com.tidal.android.util.Result
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: TidalRepository) : ViewModel() {

    private val _searchResults = MutableLiveData<Result<List<*>>>()
    val searchResults: LiveData<Result<List<*>>> = _searchResults

    private val _downloadQueue = MutableLiveData<List<Track>>(mutableListOf())
    val downloadQueue: LiveData<List<Track>> = _downloadQueue

    private val queue = mutableListOf<Track>()

    fun searchArtists(query: String) {
        viewModelScope.launch {
            _searchResults.value = Result.Loading
            _searchResults.value = repository.searchArtists(query)
        }
    }

    fun searchAlbums(query: String) {
        viewModelScope.launch {
            _searchResults.value = Result.Loading
            _searchResults.value = repository.searchAlbums(query)
        }
    }

    fun searchTracks(query: String) {
        viewModelScope.launch {
            _searchResults.value = Result.Loading
            _searchResults.value = repository.searchTracks(query)
        }
    }

    fun addTracksToQueue(tracks: List<Track>) {
        queue.addAll(tracks)
        _downloadQueue.value = queue.toList()
    }

    fun removeTrackFromQueue(track: Track) {
        queue.remove(track)
        _downloadQueue.value = queue.toList()
    }

    fun clearQueue() {
        queue.clear()
        _downloadQueue.value = emptyList()
    }

    fun getSelectedTracks(): List<Track> {
        return queue.filter { it.selected }
    }
}