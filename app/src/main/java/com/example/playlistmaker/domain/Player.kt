package com.example.playlistmaker.domain

interface Player {
    fun startAudio()
    fun pauseAudio()
    fun isPlaying(): Boolean
    fun updateProgressTime()
}