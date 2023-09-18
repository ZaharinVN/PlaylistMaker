package com.example.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.example.playlistmaker.player.domain.api.PlayerInteractor

class MediaPresenter(
    private val btnPlay: ImageButton,
    private val btnPause: ImageButton,
    private val progressTime: TextView,
    private val btnFavorite: ImageButton,
    private val btnDisLike: ImageButton,
    private val previewUrl: String?,
    private val playerInteractor: PlayerInteractor,
) : MediaContract.Presenter {
    private val progressHandler = Handler(Looper.getMainLooper())
    private lateinit var progressRunnable: Runnable

    init {
        progressRunnable = Runnable {
            updateProgressTime()
            progressHandler.postDelayed(progressRunnable, 1000)
        }

        playerInteractor.preparePlayer(previewUrl ?: "", {}, {
            progressTime.text = "00:00"
            progressHandler.removeCallbacks(progressRunnable)
            btnPlay.visibility = View.VISIBLE
            btnPause.visibility = View.GONE
        })
    }

    private fun updateProgressTime() {
        val progress = playerInteractor.currentPosition()
        progressTime.text = formatTime(progress)
    }

    private fun formatTime(timeInMillis: Int): String {
        val minutes = timeInMillis / 1000 / 60
        val seconds = timeInMillis / 1000 % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onPlayClicked() {
        btnPause.visibility = View.VISIBLE
        playerInteractor.startAudio()
        progressHandler.post(progressRunnable)
    }

    override fun onPauseAudioClicked() {
        btnPause.visibility = View.GONE
        playerInteractor.pauseAudio()
        progressHandler.removeCallbacks(progressRunnable)
    }

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
}





