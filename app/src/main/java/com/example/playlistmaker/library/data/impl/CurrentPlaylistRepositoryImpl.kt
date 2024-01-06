package com.example.playlistmaker.library.data.impl

import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.domain.db.CurrentPlaylistRepository
import com.example.playlistmaker.search.domain.model.Track

import com.example.playlistmaker.search.domain.model.mapToTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CurrentPlaylistRepositoryImpl(private val appDatabase: AppDatabase) :
    CurrentPlaylistRepository {
    override suspend fun getTracksForCurrentPlaylist(ids: List<Int>): Flow<List<Track>> {
        return flow {
            val currentPlaylistTracks = appDatabase.playlistTrackDao().getTracksByListIds(ids)
            emit(currentPlaylistTracks.map { playlistTrackEntity -> playlistTrackEntity.mapToTrack() })
        }
    }
}