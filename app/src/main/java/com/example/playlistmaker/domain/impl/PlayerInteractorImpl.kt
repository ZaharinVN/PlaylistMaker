package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.Player
import com.example.playlistmaker.domain.PlayerInteractor

class PlayerInteractorImpl(private val player: Player) : PlayerInteractor {

    private var currentPosition: Int = 0
    private var playbackState: PlayerState = PlayerState.DEFAULT

    override fun startAudio() {
        player.startAudio()
        playbackState = PlayerState.PLAYING
    }

    override fun pauseAudio() {
        player.pauseAudio()
        currentPosition = player.currentPosition()
        playbackState = PlayerState.PAUSED
    }

    override fun isPlaying(): Boolean {
        return player.isPlaying()
    }

    override fun currentPosition(): Int {
        return player.currentPosition()
    }

    override fun preparePlayer(dataSource: String, onPreparedListener: () -> Unit, onCompletionListener: () -> Unit) {
        player.preparePlayer(dataSource, onPreparedListener, onCompletionListener)
        playbackState = PlayerState.PREPARED
    }

    override fun playbackControl(onStartPlayer: () -> Unit, onPausePlayer: () -> Unit) {
        when (playbackState) {
            PlayerState.PLAYING -> {
                onPausePlayer()
                pauseAudio()
                playbackState = PlayerState.PAUSED
            }
            PlayerState.PREPARED, PlayerState.PAUSED -> {
                onStartPlayer()
                startAudio()
                playbackState = PlayerState.PLAYING
            }
            PlayerState.DEFAULT -> {}
        }
    }

    enum class PlayerState {
        DEFAULT, PREPARED, PLAYING, PAUSED
    }
}


