package com.example.playlistmaker.search.domain.model

import com.example.playlistmaker.search.data.dto.TrackDto
import java.io.Serializable

data class TrackSearchModel(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    var isFavorite: Boolean = false
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is TrackDto)
            return false
        return trackId == other.trackId
    }
}

