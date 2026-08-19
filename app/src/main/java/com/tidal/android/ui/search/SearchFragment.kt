package com.tidal.android.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.searchview.MaterialSearchView
import com.tidal.android.TidalApplication
import com.tidal.android.databinding.FragmentSearchBinding
import com.tidal.android.ui.search.adapter.SearchAdapter
import com.tidal.android.util.Result

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels {
        SearchViewModelFactory(TidalApplication.repository)
    }
    private lateinit var adapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchView()
        observeSearchResults()
    }

    private fun setupRecyclerView() {
        adapter = SearchAdapter { track ->
            viewModel.addToQueue(track)
            Toast.makeText(
                requireContext(),
                "${track.title} added to queue",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.searchResultsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@SearchFragment.adapter
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(
            object : MaterialSearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null && query.isNotEmpty()) {
                        performSearch(query)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            }
        )
    }

    private fun performSearch(query: String) {
        // Determine search type based on UI selection or default to tracks
        when (binding.searchTypeGroup.checkedButtonId) {
            binding.searchTracksButton.id -> viewModel.searchTracks(query)
            binding.searchArtistsButton.id -> viewModel.searchArtists(query)
            binding.searchAlbumsButton.id -> viewModel.searchAlbums(query)
            else -> viewModel.searchTracks(query)
        }
    }

    private fun observeSearchResults() {
        viewModel.searchResults.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.searchResultsRecyclerView.visibility = View.GONE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.searchResultsRecyclerView.visibility = View.VISIBLE
                    adapter.submitList(result.data)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.searchResultsRecyclerView.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Search failed: ${result.exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}