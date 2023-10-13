package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.ItunesSearchResult
import com.example.playlistmaker.search.domain.HistoryInteractor
import com.example.playlistmaker.search.domain.SearchUseCase

class SearchViewModel(
    private val historyInteractor: HistoryInteractor,
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    private val _searchHistory = MutableLiveData<List<ItunesSearchResult>>()
    val searchHistory: LiveData<List<ItunesSearchResult>> = _searchHistory

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    fun loadSearchHistory(historyInteractor: HistoryInteractor, searchUseCase: SearchUseCase) {
        _searchHistory.value = this.historyInteractor.loadSearchHistory()
    }

    fun saveSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getSearchQuery(): String {
        return _searchQuery.value ?: ""
    }
}


