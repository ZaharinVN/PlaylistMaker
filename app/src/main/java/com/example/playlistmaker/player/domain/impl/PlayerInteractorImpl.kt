package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.player.domain.api.PlayerInteractor

class PlayerInteractorImpl(private val playerRepository: PlayerRepository) : PlayerInteractor {

    override fun startAudio() {
        playerRepository.startAudio()
    }

    override fun pauseAudio() {
        playerRepository.pauseAudio()
    }

    override fun isPlaying(): Boolean {
        return playerRepository.isPlaying()
    }

    override fun currentPosition(): Int {
        return playerRepository.currentPosition()
    }

    override fun preparePlayer(url: String, onPreparedListener: () -> Unit) {
        playerRepository.preparePlayer(url, onPreparedListener)
    }

    override fun setOnCompletionListener(onCompletionListener: () -> Unit) {
        playerRepository.setOnCompletionListener(onCompletionListener)
    }

    override fun playbackControl(onStartPlayer: () -> Unit, onPausePlayer: () -> Unit) {
        playerRepository.playbackControl(onStartPlayer, onPausePlayer)
    }

    override fun destroyPlayer() {
        playerRepository.destroyPlayer()
    }
}


