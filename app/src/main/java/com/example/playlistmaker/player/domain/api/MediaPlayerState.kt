package com.example.playlistmaker.player.domain.api

sealed interface MediaPlayerState {
    object Default : MediaPlayerState
    object Prepared : MediaPlayerState
    data class Playing(
        val time: Int
    ) : MediaPlayerState
    object Paused : MediaPlayerState
}