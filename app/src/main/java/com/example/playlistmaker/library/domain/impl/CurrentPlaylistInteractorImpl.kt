package com.example.playlistmaker.library.domain.impl

import com.example.playlistmaker.library.domain.db.CurrentPlaylistInteractor
import com.example.playlistmaker.library.domain.db.CurrentPlaylistRepository
import com.example.playlistmaker.search.domain.model.TrackSearchModel
import kotlinx.coroutines.flow.Flow

class CurrentPlaylistInteractorImpl(
    private val currentPlaylistRepository: CurrentPlaylistRepository
) : CurrentPlaylistInteractor {
    override suspend fun getTracksForCurrentPlaylist(ids: List<Int>): Flow<List<TrackSearchModel>> {
        return currentPlaylistRepository.getTracksForCurrentPlaylist(ids)
    }
}