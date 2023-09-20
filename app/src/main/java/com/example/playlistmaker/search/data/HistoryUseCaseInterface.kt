package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.ItunesSearchResult

interface HistoryUseCaseInterface {
    fun addTrackToHistory(track: ItunesSearchResult)
    fun clearSearchHistory()
    fun saveSearchHistory()
    fun loadSearchHistory(): MutableList<ItunesSearchResult>
}