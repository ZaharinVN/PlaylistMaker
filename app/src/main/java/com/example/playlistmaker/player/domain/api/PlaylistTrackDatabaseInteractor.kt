package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.model.TrackSearchModel

interface PlaylistTrackDatabaseInteractor {
    suspend fun insertTrackToPlaylistTrackDatabase(track: TrackSearchModel)
    fun isTrackInPlaylist(playlistTracks: ArrayList<Int>, track: TrackSearchModel?): Boolean
    suspend fun deletePlaylistTrackFromDatabase(track: TrackSearchModel)

    suspend fun deletePlaylistTrackFromDatabaseById(id: Int)
}