package com.tidal.android.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tidal.android.download.TidalDownloadManager
import com.tidal.android.model.Track
import com.tidal.android.repository.TidalRepository
import com.tidal.android.util.Result
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: TidalRepository,
    private val downloadManager: TidalDownloadManager? = null
) : ViewModel() {

    private val _searchResults = MutableLiveData<Result<List<Any>>>()
    val searchResults: LiveData<Result<List<Any>>> = _searchResults

    private val _queue = MutableLiveData<List<Track>>(emptyList())
    val queue: LiveData<List<Track>> = _queue

    fun searchTracks(query: String) {
        _searchResults.value = Result.Loading()
        viewModelScope.launch {
            val result = repository.searchTracks(query)
            _searchResults.value = when (result) {
                is Result.Success -> Result.Success(result.data as List<Any>)
                is Result.Error -> Result.Error(result.exception)
                is Result.Loading -> Result.Loading()
            }
        }
    }

    fun searchArtists(query: String) {
        _searchResults.value = Result.Loading()
        viewModelScope.launch {
            val result = repository.searchArtists(query)
            _searchResults.value = when (result) {
                is Result.Success -> Result.Success(result.data as List<Any>)
                is Result.Error -> Result.Error(result.exception)
                is Result.Loading -> Result.Loading()
            }
        }
    }

    fun searchAlbums(query: String) {
        _searchResults.value = Result.Loading()
        viewModelScope.launch {
            val result = repository.searchAlbums(query)
            _searchResults.value = when (result) {
                is Result.Success -> Result.Success(result.data as List<Any>)
                is Result.Error -> Result.Error(result.exception)
                is Result.Loading -> Result.Loading()
            }
        }
    }

    suspend fun addToQueue(track: Track) {
        val currentQueue = _queue.value ?: emptyList()
        _queue.value = currentQueue + track
        downloadManager?.downloadTracks(listOf(track))
    }
}

class SearchViewModelFactory(
    private val repository: TidalRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SearchViewModel(repository) as T
    }
}
