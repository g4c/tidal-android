package com.tidal.android.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.tidal.android.databinding.FragmentSearchBinding
import com.tidal.android.model.QualityMode
import com.tidal.android.model.Track
import com.tidal.android.service.impl.TidalServiceImpl
import com.tidal.android.download.TidalDownloadManager
import com.tidal.android.repository.TidalRepository
import com.tidal.android.ui.search.adapter.SearchResultsAdapter

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var searchAdapter: SearchResultsAdapter
    private var selectedQuality: QualityMode = QualityMode.NORMAL

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        // Initialize ViewModel
        val tidalService = TidalServiceImpl(requireContext())
        val repository = TidalRepository(tidalService)
        val downloadManager = TidalDownloadManager(requireContext())
        val viewModelFactory = SearchViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)

        // Setup quality selector
        setupQualitySelector()

        // Setup search results adapter
        searchAdapter = SearchResultsAdapter { track ->
            onTrackSelected(track)
        }
        binding.searchResultsRecyclerView.adapter = searchAdapter

        // Setup search
        binding.searchButton.setOnClickListener {
            val query = binding.searchInput.text.toString().trim()
            if (query.isNotEmpty()) {
                viewModel.searchTracks(query)
            }
        }

        // Observe search results
        viewModel.searchResults.observe(viewLifecycleOwner) { result ->
            when (result) {
                is com.tidal.android.util.Result.Success -> {
                    val tracks = result.data.filterIsInstance<Track>()
                    searchAdapter.submitList(tracks)
                }
                is com.tidal.android.util.Result.Error -> {
                    // Show error
                }
                is com.tidal.android.util.Result.Loading -> {
                    // Show loading
                }
            }
        }

        return binding.root
    }

    private fun setupQualitySelector() {
        val qualities = listOf(
            QualityMode.NORMAL,
            QualityMode.LOSSLESS,
            QualityMode.HI_RES
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            qualities.map { it.displayName }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.qualitySpinner.adapter = adapter
        binding.qualitySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedQuality = qualities[position]
                viewModel.setDownloadQuality(selectedQuality)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun onTrackSelected(track: Track) {
        // Show Hi-Res marker if available
        val hiResMarker = track.getHiResMarker()
        if (hiResMarker.isNotEmpty()) {
            // Display HiRes badge to user
        }

        // Add to queue with selected quality
        viewModel.addToQueueWithQuality(track, selectedQuality)
    }
}
