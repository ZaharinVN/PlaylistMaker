package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.ItunesSearchResult
import com.example.playlistmaker.search.domain.HistoryUseCase
import com.example.playlistmaker.search.domain.SearchUseCase

class SearchViewModel(private val historyUseCase: HistoryUseCase,
                      private val searchUseCase: SearchUseCase) : ViewModel() {

    private val _searchHistory = MutableLiveData<List<ItunesSearchResult>>()
    val searchHistory: LiveData<List<ItunesSearchResult>> = _searchHistory

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    fun init(historyUseCase: HistoryUseCase, searchUseCase: SearchUseCase) {
        _searchHistory.value = this.historyUseCase.loadSearchHistory()
    }

    fun saveSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getSearchQuery(): String {
        return _searchQuery.value ?: ""
    }
}


