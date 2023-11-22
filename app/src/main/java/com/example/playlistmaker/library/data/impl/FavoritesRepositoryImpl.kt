package com.example.playlistmaker.library.data.impl

import com.example.playlistmaker.library.data.converters.TrackDbConverter
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.data.db.entity.TrackEntity
import com.example.playlistmaker.library.domain.db.FavoritesRepository
import com.example.playlistmaker.search.data.dto.TrackDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConverter,
) : FavoritesRepository {
    override fun favoritesTracks(): Flow<List<TrackDto>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTracksEntity(tracks))
    }

    private fun convertFromTracksEntity(tracks: List<TrackEntity>): List<TrackDto> {
        return tracks.map { tracks -> trackDbConvertor.map(tracks) }
    }
}
