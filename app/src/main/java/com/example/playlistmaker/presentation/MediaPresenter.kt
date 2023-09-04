package com.example.playlistmaker.presentation

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.example.playlistmaker.presentation.ui.SearchActivity


class MediaPresenter(
    private val btnPlay: ImageButton,
    private val btnPause: ImageButton,
    private val progressTime: TextView,
    private val btnFavorite: ImageButton,
    private val btnDisLike: ImageButton,
    private val previewUrl: String,
) : MediaContract.Presenter {

    private var mediaPlayer = MediaPlayer()
    private var currentPosition: Int = 0
    private var playbackState: Int = DEFAULT
    private val progressHandler = Handler(Looper.getMainLooper())
    private lateinit var progressRunnable: Runnable

    companion object {
        const val DEFAULT = 0
        const val PREPARED = 1
        const val PLAYING = 2
        const val PAUSED = 3
    }

    override fun onPlayClicked() {
        btnPause.visibility = View.VISIBLE
        when (playbackState) {
            DEFAULT -> {
                playAudio(previewUrl)
                Log.d("MediaPresenter", "previewUrl-MP-PC: $previewUrl")
            }

            PREPARED -> {
                resumeAudio()
                btnPause.visibility = View.GONE
            }

            PLAYING -> {
                playAudio(previewUrl)
                btnPause.visibility = View.VISIBLE
            }

            PAUSED -> {
                resumeAudio()
            }
        }
    }

    override fun onPauseAudioClicked() {
        btnPause.visibility = View.GONE
        pauseAudio()
    }

    override fun onFavoriteClicked() {
        btnDisLike.visibility = View.VISIBLE
    }

    override fun onDisLikeClicked() {
        btnDisLike.visibility = View.GONE
    }

    private fun updateProgressTime(progressTime: TextView) {
        val progress = mediaPlayer?.currentPosition
        progressTime.text = "${formatTime(progress!!)}"
    }

    private fun formatTime(timeInMillis: Int): String {
        val minutes = timeInMillis / 1000 / 60
        val seconds = timeInMillis / 1000 % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun progressRunnable() {
        updateProgressTime(progressTime)
        progressHandler.postDelayed(progressRunnable, 1000)
    }

    private fun playAudio(previewUrl: String?) {
        Log.d("MediaPresenter", "previewUrl-MP-PA: $previewUrl")
        try {
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                setDataSource(previewUrl)
                prepareAsync()
                setOnPreparedListener { mp ->
                    mediaPlayer = mp
                    startAudio()
                }
                setOnCompletionListener {
                    btnPause.visibility = View.GONE
                    playbackState = DEFAULT
                    progressTime.text = "00:00"
                    progressHandler.removeCallbacks(progressRunnable)
                }
                progressHandler.post(progressRunnable)
            }
            playbackState = PREPARED
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startAudio() {
        mediaPlayer?.start()
        playbackState = PLAYING
    }

    private fun pauseAudio() {
        mediaPlayer?.pause()
        currentPosition = mediaPlayer?.currentPosition ?: 0
        playbackState = PAUSED
        progressHandler.removeCallbacks(progressRunnable)
    }

    private fun resumeAudio() {
        mediaPlayer?.seekTo(currentPosition)
        mediaPlayer?.start()
        playbackState = PLAYING
        progressHandler.post(progressRunnable)
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        progressHandler.removeCallbacks(progressRunnable)
    }
}
