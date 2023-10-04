package com.example.playlistmaker.player.ui

data class MediaViewState(
    val trackCoverUrl: String?,
    val trackName: String?,
    val artistName: String?,
    val trackTime: Long?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val progressTime: String
)