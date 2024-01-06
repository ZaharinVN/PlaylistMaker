package com.example.playlistmaker.library.container

import com.example.playlistmaker.search.domain.model.Track

data class PlaylistInfoContainer(
    val totalTime: String,
    val playlistTracks: List<Track>
)