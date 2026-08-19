package com.tidal.android.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tidal.android.R
import com.tidal.android.TidalApplication
import com.tidal.android.model.Album
import com.tidal.android.model.Artist
import com.tidal.android.model.Track
import com.tidal.android.util.Result

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var searchTypeSpinner: Spinner
    private lateinit var searchQueryEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var searchResultsListView: ListView
    private lateinit var queueListView: ListView
    private lateinit var addToQueueButton: Button

    private var resultsAdapter: ArrayAdapter<*>? = null
    private var queueAdapter: ArrayAdapter<Track>? = null
    private var selectedResults = mutableListOf<Any>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        searchTypeSpinner = view.findViewById(R.id.search_type_spinner)
        searchQueryEditText = view.findViewById(R.id.search_query_edittext)
        searchButton = view.findViewById(R.id.search_button)
        searchResultsListView = view.findViewById(R.id.search_results_listview)
        queueListView = view.findViewById(R.id.queue_listview)
        addToQueueButton = view.findViewById(R.id.add_to_queue_button)

        // Setup ViewModel factory
        val factory = SearchViewModelFactory(TidalApplication.repository)
        viewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)

        // Setup search type spinner
        val searchTypes = arrayOf("Artists", "Albums", "Tracks")
        ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, searchTypes).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            searchTypeSpinner.adapter = it
        }

        // Setup queue adapter
        queueAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1)
        queueListView.adapter = queueAdapter

        // Observe search results
        viewModel.searchResults.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    Toast.makeText(requireContext(), "Searching...", Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    selectedResults = result.data.toMutableList()
                    @Suppress("UNCHECKED_CAST")
                    resultsAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        result.data
                    ) as ArrayAdapter<*>
                    searchResultsListView.adapter = resultsAdapter
                }
                is Result.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Error: ${result.exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // Observe queue
        viewModel.downloadQueue.observe(viewLifecycleOwner) { queue ->
            queueAdapter?.clear()
            queueAdapter?.addAll(queue)
            queueAdapter?.notifyDataSetChanged()
        }

        // Search button click listener
        searchButton.setOnClickListener {
            val query = searchQueryEditText.text.toString()
            if (query.isNotEmpty()) {
                when (searchTypeSpinner.selectedItemPosition) {
                    0 -> viewModel.searchArtists(query)
                    1 -> viewModel.searchAlbums(query)
                    2 -> viewModel.searchTracks(query)
                }
            } else {
                Toast.makeText(requireContext(), "Enter a search query", Toast.LENGTH_SHORT).show()
            }
        }

        // Add to queue button click listener
        addToQueueButton.setOnClickListener {
            val selectedTracks = mutableListOf<Track>()
            when (searchTypeSpinner.selectedItemPosition) {
                0 -> {
                    // For artists, would need to fetch albums and tracks
                }
                1 -> {
                    // For albums, would need to fetch tracks
                }
                2 -> {
                    // For tracks, add directly
                    for (item in selectedResults) {
                        if (item is Track) {
                            selectedTracks.add(item)
                        }
                    }
                    if (selectedTracks.isNotEmpty()) {
                        viewModel.addTracksToQueue(selectedTracks)
                        Toast.makeText(
                            requireContext(),
                            "${selectedTracks.size} tracks added to queue",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        // Queue item long click to remove
        queueListView.setOnItemLongClickListener { _, _, position, _ ->
            val track = queueAdapter?.getItem(position) as? Track
            if (track != null) {
                viewModel.removeTrackFromQueue(track)
                Toast.makeText(requireContext(), "Track removed from queue", Toast.LENGTH_SHORT)
                    .show()
            }
            true
        }
    }
}