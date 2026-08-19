package com.tidal.android.model

import com.google.gson.annotations.SerializedName

data class Track(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("artist")
    val artist: Artist,
    @SerializedName("album")
    val album: Album,
    @SerializedName("trackNumber")
    val trackNumber: Int = 0,
    @SerializedName("duration")
    val duration: Int = 0,
    @SerializedName("url")
    val url: String = "",
    var selected: Boolean = false
) {
    fun getDurationFormatted(): String {
        val minutes = duration / 60
        val seconds = duration % 60
        return String.format("%d:%02d", minutes, seconds)
    }

    override fun toString(): String = title
}