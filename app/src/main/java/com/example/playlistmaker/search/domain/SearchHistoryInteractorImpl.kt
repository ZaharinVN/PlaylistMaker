package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.ItunesSearchResult

class SearchHistoryInteractorImpl : HistoryInteractor {

    override fun addTrackToHistory(track: ItunesSearchResult) {
        // Добавление трека в историю поиска
    }

    override fun clearSearchHistory() {
        // Очистка истории поиска
    }

    override fun saveSearchHistory() {
        // Сохранение истории поиска в SharedPreferences
    }

    override fun loadSearchHistory(): MutableList<ItunesSearchResult> {
        // Загрузка истории поиска из SharedPreferences
        return mutableListOf()
    }
}