package com.example.playlistmaker

import android.content.Intent
import com.example.playlistmaker.data.PlayerImpl
import com.example.playlistmaker.data.dto.MediaDataSource
import com.example.playlistmaker.domain.Player
import com.example.playlistmaker.domain.PlayerInteractor
import com.example.playlistmaker.domain.api.MediaRepository
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl


object Creator {

    fun createMediaRepository(intent: Intent): MediaRepository {
        return MediaDataSource(intent)
    }
    fun createPlayer(audioUrl: String?): Player {
        return PlayerImpl(audioUrl)
    }
    fun createInteractor(player: Player): PlayerInteractor {
        return PlayerInteractorImpl(player)
    }
}
