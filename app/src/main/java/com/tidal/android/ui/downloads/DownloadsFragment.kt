package com.tidal.android.ui.downloads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tidal.android.TidalApplication
import com.tidal.android.databinding.FragmentDownloadsBinding
import com.tidal.android.download.DownloadStatus
import com.tidal.android.ui.downloads.adapter.DownloadsAdapter
import kotlinx.coroutines.launch

class DownloadsFragment : Fragment() {

    private lateinit var binding: FragmentDownloadsBinding
    private val viewModel: DownloadsViewModel by viewModels {
        DownloadsViewModelFactory(TidalApplication.downloadManager)
    }
    private lateinit var adapter: DownloadsAdapter
    private var isPaused = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDownloadsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupControlButtons()
        observeDownloads()
    }

    private fun setupRecyclerView() {
        adapter = DownloadsAdapter { taskId ->
            showCancelConfirmDialog(taskId)
        }

        binding.downloadsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@DownloadsFragment.adapter
        }
    }

    private fun setupControlButtons() {
        binding.pauseButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.pauseDownloads()
                isPaused = true
                updateButtonStates()
                Toast.makeText(
                    requireContext(),
                    "Downloads paused",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.resumeButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.resumeDownloads()
                isPaused = false
                updateButtonStates()
                Toast.makeText(
                    requireContext(),
                    "Downloads resumed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun observeDownloads() {
        viewModel.activeDownloads.observe(viewLifecycleOwner) { downloads ->
            if (downloads.isEmpty()) {
                binding.downloadsRecyclerView.visibility = View.GONE
                binding.emptyStateTextView.visibility = View.VISIBLE
            } else {
                binding.downloadsRecyclerView.visibility = View.VISIBLE
                binding.emptyStateTextView.visibility = View.GONE
                adapter.submitList(downloads)
            }
            updateDownloadStats(downloads)
        }
    }

    private fun updateDownloadStats(downloads: List<Any>) {
        val totalSize = downloads.size
        val completed = downloads.count {
            it.toString().contains(DownloadStatus.COMPLETED.name)
        }
        binding.statsTextView.text = "$completed / $totalSize completed"
    }

    private fun updateButtonStates() {
        binding.pauseButton.isEnabled = !isPaused
        binding.resumeButton.isEnabled = isPaused
    }

    private fun showCancelConfirmDialog(taskId: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Cancel Download")
            .setMessage("Are you sure you want to cancel this download?")
            .setNegativeButton("No", null)
            .setPositiveButton("Yes") { _, _ ->
                lifecycleScope.launch {
                    viewModel.cancelDownload(taskId)
                    Toast.makeText(
                        requireContext(),
                        "Download cancelled",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .show()
    }
}