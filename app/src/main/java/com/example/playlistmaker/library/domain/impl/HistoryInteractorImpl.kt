package com.example.playlistmaker.library.domain.impl

import com.example.playlistmaker.library.domain.db.HistoryInteractor
import com.example.playlistmaker.library.domain.db.HistoryRepository
import com.example.playlistmaker.search.data.dto.TrackDto
import kotlinx.coroutines.flow.Flow

class HistoryInteractorImpl(private val historyRepository: HistoryRepository) : HistoryInteractor {
    override fun historyTracks(): Flow<List<TrackDto>> {
        return historyRepository.historyTracks()
    }
}