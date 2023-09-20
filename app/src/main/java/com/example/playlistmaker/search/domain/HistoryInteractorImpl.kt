package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.ItunesSearchResult

class HistoryInteractorImpl(private val searchHistoryInteractor: HistoryInteractor) : HistoryInteractor {

    override fun addTrackToHistory(track: ItunesSearchResult) {
        searchHistoryInteractor.addTrackToHistory(track)
    }

    override fun clearSearchHistory() {
        searchHistoryInteractor.clearSearchHistory()
    }

    override fun saveSearchHistory() {
        searchHistoryInteractor.saveSearchHistory()
    }

    override fun loadSearchHistory(): MutableList<ItunesSearchResult> {
        return searchHistoryInteractor.loadSearchHistory()
    }
}