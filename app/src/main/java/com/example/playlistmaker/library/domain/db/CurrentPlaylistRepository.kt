package com.example.playlistmaker.library.domain.db

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface CurrentPlaylistRepository {
    suspend fun getTracksForCurrentPlaylist(ids: List<Int>): Flow<List<Track>>
}