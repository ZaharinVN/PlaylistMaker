package com.example.playlistmaker.library.data.impl

import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.domain.db.PlaylistMediaDatabaseRepository
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.domain.models.mapToPlaylist
import com.example.playlistmaker.library.domain.models.mapToPlaylistEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistMediaDatabaseRepositoryImpl(private val appDatabase: AppDatabase) :
    PlaylistMediaDatabaseRepository {

    override suspend fun getPlaylistsFromDatabase(): Flow<List<Playlist>> = flow {
        val playlistEntityList = appDatabase.playlistDao().getPlaylists()
        emit(playlistEntityList.map { playlistEntity -> playlistEntity.mapToPlaylist() })
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().deletePlaylist(playlist.mapToPlaylistEntity())
    }

}