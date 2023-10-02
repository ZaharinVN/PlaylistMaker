package com.example.playlistmaker.search.domain

interface HistoryUseCase {
    fun addTrackToHistory(track: ItunesSearchResult)
    fun clearSearchHistory()
    fun saveSearchHistory()
    fun loadSearchHistory(): MutableList<ItunesSearchResult> = mutableListOf()
}