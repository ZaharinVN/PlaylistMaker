package com.example.playlistmaker.library.ui.history

import com.example.playlistmaker.search.data.dto.TrackDto

sealed interface HistoryState {
    object Loading : HistoryState

    data class Content(
        val tracks: List<TrackDto>
    ) : HistoryState

    data class Empty(
        val image: Int,
        val message: String
    ) : HistoryState
}