package com.example.playlistmaker.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.search.domain.HistoryInteractor
import com.example.playlistmaker.search.domain.SearchUseCase

class SearchViewModelFactory(
    private val historyInteractor: HistoryInteractor,
    private val searchUseCase: SearchUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(historyInteractor, searchUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}