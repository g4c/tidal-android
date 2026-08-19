package com.tidal.android.model

import com.google.gson.annotations.SerializedName

data class Artist(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
) {
    override fun toString(): String = name
}