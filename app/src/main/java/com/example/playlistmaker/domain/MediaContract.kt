package com.example.playlistmaker.domain

interface MediaContract {
    interface Presenter {
        fun onPlayClicked()
        fun onPauseAudioClicked()
        fun onFavoriteClicked()
        fun onDisLikeClicked()
        fun onPause()
    }
}
