package com.example.playlistmaker.player.ui

import java.text.SimpleDateFormat
import java.util.Locale

class TrackPlayerModel (
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
){
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    fun formatTrackDuration() = SimpleDateFormat(
        "mm:ss",
        Locale.getDefault()
    ).format(trackTimeMillis)!!

    fun formatReleaseDate() = releaseDate.substringBefore('-')
    }
