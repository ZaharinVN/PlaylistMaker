package com.example.playlistmaker.player.data

import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.player.domain.api.PlaylistTrackDatabaseRepository
import com.example.playlistmaker.search.domain.model.TrackSearchModel
import com.example.playlistmaker.search.domain.model.mapToPlaylistTrackEntity


class PlaylistTrackDatabaseRepositoryImpl(
    private val appDatabase: AppDatabase
) : PlaylistTrackDatabaseRepository {
    override suspend fun insertTrackToPlaylistTrackDatabase(track: TrackSearchModel) {
        appDatabase.playlistTrackDao().insertTrack(track.mapToPlaylistTrackEntity())
    }

    override fun isTrackInPlaylist(
        playlistTracks: ArrayList<Int>,
        track: TrackSearchModel?
    ): Boolean {
        return playlistTracks.contains(track?.trackId?.toInt())
    }

    override suspend fun deletePlaylistTrackFromDatabase(track: TrackSearchModel) {
        appDatabase.playlistTrackDao()
            .deletePlaylistTrack(track.mapToPlaylistTrackEntity(newTimeStamp = false))
    }

    override suspend fun deletePlaylistTrackFromDatabaseById(id: Int) {
        appDatabase.playlistTrackDao().deleteTrackById(id)
    }
}