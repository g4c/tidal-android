package com.tidal.android.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tidal.android.databinding.ItemSearchResultBinding
import com.tidal.android.model.Track

class SearchAdapter(
    private val onTrackClick: (Track) -> Unit
) : ListAdapter<Any, SearchAdapter.SearchViewHolder>(SearchDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchViewHolder(binding, onTrackClick)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SearchViewHolder(
        private val binding: ItemSearchResultBinding,
        private val onTrackClick: (Track) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Any) {
            when (item) {
                is Track -> {
                    binding.titleTextView.text = item.title
                    binding.subtitleTextView.text = item.artist?.name ?: "Unknown Artist"
                    binding.durationTextView.text = formatDuration(item.duration)
                    binding.root.setOnClickListener { onTrackClick(item) }
                }
                else -> {
                    binding.titleTextView.text = item.toString()
                    binding.subtitleTextView.text = ""
                }
            }
        }

        private fun formatDuration(seconds: Int): String {
            val minutes = seconds / 60
            val secs = seconds % 60
            return String.format("%d:%02d", minutes, secs)
        }
    }

    class SearchDiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is Track && newItem is Track -> oldItem.id == newItem.id
                else -> oldItem == newItem
            }
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem == newItem
        }
    }
}