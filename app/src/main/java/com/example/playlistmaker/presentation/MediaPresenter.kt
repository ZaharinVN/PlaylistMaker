package com.example.playlistmaker.presentation

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.example.playlistmaker.domain.PlayerInteractor
import com.example.playlistmaker.presentation.ui.MediaContract


class MediaPresenter(
    private val btnPlay: ImageButton,
    private val btnPause: ImageButton,
    private val progressTime: TextView,
    private val btnFavorite: ImageButton,
    private val btnDisLike: ImageButton,
    private val previewUrl: String?,
    private val playerInteractor: PlayerInteractor

) : MediaContract.Presenter {
    override fun onFavoriteClicked() {
        btnDisLike.visibility = View.VISIBLE
    }

    override fun onDisLikeClicked() {
        btnDisLike.visibility = View.GONE
    }

    override fun onPause() {
        btnPause.visibility = View.GONE
        playerInteractor.pauseAudio()
    }

    override fun onPlayClicked() {
        btnPause.visibility = View.VISIBLE
        playerInteractor.startAudio()
        if (playerInteractor.isPlaying()) {
            btnPause.visibility = View.VISIBLE
        } else {
            btnPause.visibility = View.GONE
        }
    }

    override fun onPauseAudioClicked() {
        btnPause.visibility = View.GONE
        playerInteractor.pauseAudio()
    }
}





