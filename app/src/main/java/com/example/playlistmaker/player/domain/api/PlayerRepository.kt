package com.example.playlistmaker.player.domain.api


interface PlayerRepository {
    fun startAudio()
    fun pauseAudio()
    fun isPlaying(): Boolean
    fun currentPosition(): Int
    fun playbackControl(onStartPlayer: () -> Unit, onPausePlayer: () -> Unit)
    fun preparePlayer(url: String, onPreparedListener: () -> Unit)
    fun setOnCompletionListener(onCompletionListener: () -> Unit)
    fun destroyPlayer()

}

