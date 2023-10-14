package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.dto.ResponseStatus
import com.example.playlistmaker.search.domain.model.TrackSearchModel

interface SearchRepository {
    fun searchTrack(expression: String): ResponseStatus<List<TrackSearchModel>>
    fun getTrackHistoryList(): List<TrackSearchModel>
    fun addTrackInHistory(track: TrackSearchModel)
    fun clearHistory()
}
