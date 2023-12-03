package com.example.playlistmaker.library.data.impl

import com.example.playlistmaker.library.data.converters.TrackDbConverter
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.data.db.entity.TrackEntity
import com.example.playlistmaker.library.domain.db.FavoritesRepository
import com.example.playlistmaker.search.domain.model.TrackSearchModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter,
) : FavoritesRepository {

    override fun favoritesTracks(): Flow<List<TrackSearchModel>> = flow {
        val tracks = appDatabase.trackDao().getFavoriteTracks()
        emit(convertFromTracksEntity(tracks))
    }

    private fun convertFromTracksEntity(tracks: List<TrackEntity>): List<TrackSearchModel> {
        return tracks.map { track -> trackDbConverter.map(track) }
    }

    override suspend fun addToFavorites(track: TrackSearchModel) {
        saveTrack(track)
    }

    override suspend fun deleteFromFavorites(track: TrackSearchModel) {
        deleteTrack(track)
    }

    override fun getFavoritesID(): Flow<List<String>> = flow {
        val tracksId = appDatabase.trackDao().getTracksID()
        emit(tracksId)
    }

    private suspend fun saveTrack(track: TrackSearchModel) {
        val trackEntity = trackDbConverter.map(track)
        appDatabase.trackDao().insertToFavorites(trackEntity)
    }

    private suspend fun deleteTrack(track: TrackSearchModel) {
        val trackEntity = trackDbConverter.map(track)
        appDatabase.trackDao().deleteFromFavorites(trackEntity)
    }


}
