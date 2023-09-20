package com.example.playlistmaker.search.data
import com.example.playlistmaker.search.domain.HistoryInteractor

class HistoryUseCaseImpl(private val historyInteractor: HistoryInteractor) : HistoryUseCaseInterface {

    override fun addTrackToHistory(track: ItunesSearchResult) {
        historyInteractor.addTrackToHistory(track)
    }

    override fun clearSearchHistory() {
        historyInteractor.clearSearchHistory()
    }

    override fun saveSearchHistory() {
        historyInteractor.saveSearchHistory()
    }

    override fun loadSearchHistory(): MutableList<ItunesSearchResult> {
        return historyInteractor.loadSearchHistory()
    }
}
