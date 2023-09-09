package com.example.playlistmaker.presentation

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.example.playlistmaker.data.PlayerImpl
import com.example.playlistmaker.domain.MediaContract

class MediaPresenter(
    private val btnPlay: ImageButton,
    private val btnPause: ImageButton,
    progressTime: TextView,
    private val btnFavorite: ImageButton,
    private val btnDisLike: ImageButton,
    previewUrl: String?,
) : MediaContract.Presenter, MediaContract {

    private val player: PlayerImpl = PlayerImpl(previewUrl, progressTime, btnPause)
    override fun onFavoriteClicked() {
        btnDisLike.visibility = View.VISIBLE
    }

    override fun onDisLikeClicked() {
        btnDisLike.visibility = View.GONE
    }

    override fun onPlayClicked() {
        btnPause.visibility = View.VISIBLE
        player.playOrResume()
    }

    override fun onPauseAudioClicked() {
        btnPause.visibility = View.GONE
        player.pauseAudio()
    }

    override fun onPause() {
        btnPause.visibility = View.GONE
        player.pauseAudio()
    }
}




