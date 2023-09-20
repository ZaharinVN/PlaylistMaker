package com.example.playlistmaker.search.domain

import android.content.Context
import com.example.playlistmaker.search.data.HistoryUseCaseImpl
import com.example.playlistmaker.search.data.HistoryUseCaseInterface

object UseCaseCreator {
    fun createHistoryUseCase(): HistoryUseCaseInterface {
        val historyInteractor = HistoryInteractorImpl(SearchHistoryInteractorImpl())
        return HistoryUseCaseImpl(historyInteractor)
    }
}