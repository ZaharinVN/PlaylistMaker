package com.example.playlistmaker.search.domain.model

import com.example.playlistmaker.library.data.db.entity.PlaylistTrackEntity
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TrackSearchModel(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis") val trackTimeMillis: String,
    @SerializedName("artworkUrl100") val artworkUrl100: String,
    val artworkUrl60: String?,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    var isFavorite: Boolean = false,
    val insertTimeStamp: Long? = null
) : Serializable

fun TrackSearchModel.mapToPlaylistTrackEntity(newTimeStamp: Boolean = true): PlaylistTrackEntity {
    val timeStamp = if (newTimeStamp) System.currentTimeMillis() else this.insertTimeStamp
    return PlaylistTrackEntity(
        trackId.toInt(),
        trackName,
        artistName,
        trackTimeMillis,
        artworkUrl100,
        artworkUrl60,
        collectionName,
        releaseDate,
        primaryGenreName,
        country,
        previewUrl,
        insertTimeStamp = timeStamp
    )
}
data class Track(
    val trackId: Int,
    val trackName: String?,
    val artistName: String?,
    @SerializedName("trackTimeMillis") val trackTime: String?,
    @SerializedName("artworkUrl100") val artworkUrl: String?,
    val artworkUrl60: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    val insertTimeStamp: Long? = null
): Serializable

fun PlaylistTrackEntity.mapToTrack(): Track {
    return Track(
        trackId,
        trackName,
        artistName,
        trackTime,
        artworkUrl,
        artworkUrl60,
        collectionName,
        releaseDate,
        primaryGenreName,
        country,
        previewUrl,
        insertTimeStamp
    )
}


