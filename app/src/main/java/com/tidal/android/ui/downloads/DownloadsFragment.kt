package com.tidal.android.ui.downloads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tidal.android.R
import com.tidal.android.TidalApplication
import com.tidal.android.download.DownloadTask
import com.tidal.android.model.Track
import com.tidal.android.util.Result

class DownloadsFragment : Fragment() {

    private lateinit var viewModel: DownloadsViewModel
    private lateinit var downloadsListView: ListView
    private lateinit var startDownloadsButton: Button
    private lateinit var statusTextView: TextView

    private var downloadsAdapter: DownloadsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_downloads, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        downloadsListView = view.findViewById(R.id.downloads_listview)
        startDownloadsButton = view.findViewById(R.id.start_downloads_button)
        statusTextView = view.findViewById(R.id.status_textview)

        // Setup ViewModel factory
        val factory = DownloadsViewModelFactory(TidalApplication.downloadManager)
        viewModel = ViewModelProvider(this, factory).get(DownloadsViewModel::class.java)

        // Setup downloads adapter
        downloadsAdapter = DownloadsAdapter(requireContext(), mutableListOf())
        downloadsListView.adapter = downloadsAdapter

        // Observe active tasks
        viewModel.activeTasks.observe(viewLifecycleOwner) { tasks ->
            downloadsAdapter?.clear()
            downloadsAdapter?.addAll(tasks)
            downloadsAdapter?.notifyDataSetChanged()

            // Update status
            statusTextView.text = when {
                tasks.isEmpty() -> "No active downloads"
                else -> "${tasks.size} download(s) in progress"
            }
        }

        // Observe download progress
        viewModel.downloadProgress.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    startDownloadsButton.isEnabled = false
                }
                is Result.Success -> {
                    startDownloadsButton.isEnabled = true
                    Toast.makeText(requireContext(), "Downloads started", Toast.LENGTH_SHORT)
                        .show()
                    viewModel.updateActiveTasksList()
                }
                is Result.Error -> {
                    startDownloadsButton.isEnabled = true
                    Toast.makeText(
                        requireContext(),
                        "Error: ${result.exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // Start downloads button click listener
        startDownloadsButton.setOnClickListener {
            // Get queue from search fragment (would need to pass data between fragments)
            // For now, just show a message
            Toast.makeText(requireContext(), "Start downloads from queue", Toast.LENGTH_SHORT)
                .show()
        }

        // Download item long click to cancel
        downloadsListView.setOnItemLongClickListener { _, _, position, _ ->
            val task = downloadsAdapter?.getItem(position) as? DownloadTask
            if (task != null) {
                viewModel.cancelDownload(task.id)
                Toast.makeText(requireContext(), "Download cancelled", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }
}