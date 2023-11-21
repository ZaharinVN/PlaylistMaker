package com.example.playlistmaker.library.data.impl

import com.example.playlistmaker.library.data.converters.TrackDbConverter
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.data.db.entity.TrackEntity
import com.example.playlistmaker.library.domain.db.HistoryRepository
import com.example.playlistmaker.search.data.dto.TrackDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HistoryRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val movieDbConvertor: TrackDbConverter,
) : HistoryRepository {
    override fun historyTracks(): Flow<List<TrackDto>> = flow {
        val movies = appDatabase.trackDao().getTracks()
        emit(convertFromTracksEntity(movies))
    }

    private fun convertFromTracksEntity(tracks: List<TrackEntity>): List<TrackDto> {
        return tracks.map { tracks -> movieDbConvertor.map(tracks) }
    }
}
