package com.example.playlistmaker.library.domain.impl

import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.domain.db.PlaylistDatabaseInteractor
import com.example.playlistmaker.library.domain.db.PlaylistDatabaseRepository

class PlaylistDatabaseInteractorImpl(
    private val playlistDatabaseRepository: PlaylistDatabaseRepository
) : PlaylistDatabaseInteractor {
    override suspend fun insertPlaylistToDatabase(playlist: Playlist) {
        playlistDatabaseRepository.insertPlaylistToDatabase(playlist)
    }

}