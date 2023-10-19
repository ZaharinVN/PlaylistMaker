package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.ResponseStatus
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.search.domain.model.TrackSearchModel
import java.util.concurrent.Executors

class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: SearchInteractor.SearchConsumer) {
        executor.execute {
            when (val resource = repository.searchTrack(expression)) {
                is ResponseStatus.Success -> {
                    consumer.consume(resource.data, false)
                }

                is ResponseStatus.Error -> {
                    consumer.consume(null, true)
                }
            }
        }
    }

    override fun getTracksHistory(consumer: SearchInteractor.HistoryConsumer) {
        consumer.consume(repository.getTrackHistoryList())
    }

    override fun addTrackToHistory(track: TrackSearchModel) {
        repository.addTrackInHistory(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}