package com.example.playlistmaker.search.domain

class HistoryInteractorImpl(private val historyRepository: HistoryRepository) : HistoryRepository,
    HistoryInteractor {

    override fun addTrackToHistory(track: ItunesSearchResult) {
        historyRepository.addTrackToHistory(track)
    }

    override fun clearSearchHistory() {
        historyRepository.clearSearchHistory()
    }

    override fun saveSearchHistory() {
        historyRepository.saveSearchHistory()
    }

    override fun loadSearchHistory(): MutableList<ItunesSearchResult> {
        return historyRepository.loadSearchHistory()
    }
}