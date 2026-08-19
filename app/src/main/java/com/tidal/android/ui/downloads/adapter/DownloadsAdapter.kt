package com.tidal.android.ui.downloads.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tidal.android.databinding.ItemDownloadBinding
import com.tidal.android.download.DownloadStatus
import com.tidal.android.download.DownloadTask

class DownloadsAdapter(
    private val onCancelClick: (String) -> Unit
) : ListAdapter<DownloadTask, DownloadsAdapter.DownloadViewHolder>(DownloadDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadViewHolder {
        val binding = ItemDownloadBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DownloadViewHolder(binding, onCancelClick)
    }

    override fun onBindViewHolder(holder: DownloadViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DownloadViewHolder(
        private val binding: ItemDownloadBinding,
        private val onCancelClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: DownloadTask) {
            binding.titleTextView.text = task.title
            binding.artistTextView.text = task.artist
            binding.progressBar.progress = task.progressPercent
            binding.progressTextView.text = "${task.progressPercent}%"
            binding.statusTextView.text = task.status.name
            binding.sizeTextView.text = formatSize(task.fileSize)

            // Update status color
            when (task.status) {
                DownloadStatus.DOWNLOADING -> binding.statusTextView.setTextColor(
                    binding.root.context.getColor(android.R.color.holo_blue_light)
                )
                DownloadStatus.COMPLETED -> binding.statusTextView.setTextColor(
                    binding.root.context.getColor(android.R.color.holo_green_light)
                )
                DownloadStatus.FAILED -> binding.statusTextView.setTextColor(
                    binding.root.context.getColor(android.R.color.holo_red_light)
                )
                else -> binding.statusTextView.setTextColor(
                    binding.root.context.getColor(android.R.color.darker_gray)
                )
            }

            binding.cancelButton.apply {
                isEnabled = task.status != DownloadStatus.COMPLETED
                setOnClickListener { onCancelClick(task.id) }
            }
        }

        private fun formatSize(bytes: Long): String {
            return when {
                bytes < 1024 -> "$bytes B"
                bytes < 1024 * 1024 -> "${bytes / 1024} KB"
                else -> "${bytes / (1024 * 1024)} MB"
            }
        }
    }

    class DownloadDiffCallback : DiffUtil.ItemCallback<DownloadTask>() {
        override fun areItemsTheSame(oldItem: DownloadTask, newItem: DownloadTask): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DownloadTask, newItem: DownloadTask): Boolean {
            return oldItem == newItem
        }
    }
}