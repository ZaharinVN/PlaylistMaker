package com.example.playlistmaker.library.domain.db

import com.example.playlistmaker.search.data.dto.TrackDto
import kotlinx.coroutines.flow.Flow

interface HistoryInteractor {

    fun historyTracks(): Flow<List<TrackDto>>
}