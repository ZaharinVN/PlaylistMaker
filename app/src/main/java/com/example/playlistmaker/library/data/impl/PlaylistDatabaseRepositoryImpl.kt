package com.example.playlistmaker.library.data.impl

import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.domain.db.PlaylistDatabaseRepository
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.domain.models.mapToPlaylistEntity

class PlaylistDatabaseRepositoryImpl(private val appDatabase: AppDatabase) :
    PlaylistDatabaseRepository {
    override suspend fun insertPlaylistToDatabase(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(playlist.mapToPlaylistEntity())
    }
}