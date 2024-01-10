package com.example.playlistmaker.library.domain.db

import com.example.playlistmaker.library.domain.models.Playlist

interface PlaylistDatabaseRepository {
    suspend fun insertPlaylistToDatabase(playlist: Playlist)
}