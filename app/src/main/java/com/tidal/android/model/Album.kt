package com.tidal.android.model

import com.google.gson.annotations.SerializedName

data class Album(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("artists")
    val artists: List<Artist> = emptyList(),
    @SerializedName("releaseDate")
    val releaseDate: String? = null,
    @SerializedName("cover")
    val cover: String? = null
) {
    fun imageUrl(): String? {
        if (cover.isNullOrEmpty()) {
            return null
        }
        return if (cover.startsWith("http")) {
            cover
        } else {
            "https://images.tidal.com/im/$cover/640x640.jpg"
        }
    }

    override fun toString(): String = title
}