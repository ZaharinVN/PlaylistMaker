package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.ItunesSearchResult

interface HistoryInteractor {
    fun addTrackToHistory(track: ItunesSearchResult)
    fun clearSearchHistory()
    fun saveSearchHistory()
    fun loadSearchHistory(): MutableList<ItunesSearchResult>
}