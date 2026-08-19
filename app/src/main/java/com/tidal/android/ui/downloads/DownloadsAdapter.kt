package com.tidal.android.ui.downloads

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.TextView
import com.tidal.android.R
import com.tidal.android.download.DownloadTask

class DownloadsAdapter(context: Context, tasks: List<DownloadTask>) :
    ArrayAdapter<DownloadTask>(context, 0, tasks) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val task = getItem(position) ?: return convertView ?: View(context)

        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_download, parent, false)

        val titleTextView = view.findViewById<TextView>(R.id.download_title_textview)
        val progressTextView = view.findViewById<TextView>(R.id.download_progress_textview)
        val statusTextView = view.findViewById<TextView>(R.id.download_status_textview)
        val progressBar = view.findViewById<ProgressBar>(R.id.download_progress_bar)

        titleTextView.text = task.track.title
        progressTextView.text = task.getProgressFormatted()
        statusTextView.text = task.status.name
        progressBar.progress = task.progress

        return view
    }
}