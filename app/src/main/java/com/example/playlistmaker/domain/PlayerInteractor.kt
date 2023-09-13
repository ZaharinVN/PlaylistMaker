package com.example.playlistmaker.domain

interface PlayerInteractor {
    fun startAudio()
    fun pauseAudio()
    fun isPlaying(): Boolean
    fun updateProgressTime()

}