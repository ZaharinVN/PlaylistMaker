package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.ItunesSearchResult

class HistoryUseCaseImpl(private val historyRepository: HistoryRepository) : HistoryRepository,
    HistoryUseCase {

    override fun addTrackToHistory(track: ItunesSearchResult) {
        historyRepository.addTrackToHistory(track)
    }

    override fun clearSearchHistory() {
        historyRepository.clearSearchHistory()
    }

    override fun saveSearchHistory() {
        historyRepository.saveSearchHistory()
    }

    override fun loadSearchHistory(): MutableList<ItunesSearchResult>  {
        return historyRepository.loadSearchHistory()
    }
}