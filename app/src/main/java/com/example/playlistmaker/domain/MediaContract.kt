package com.example.playlistmaker.domain

interface MediaContract {
    interface Presenter {
        //методы, которые будут использоваться из Activity для обработки пользовательских действий.
        fun onPlayClicked()
        fun onPauseAudioClicked()
        fun onFavoriteClicked()
        fun onDisLikeClicked()
        fun progressRunnable()
        fun onPause()
        fun onDestroy()
    }
}
