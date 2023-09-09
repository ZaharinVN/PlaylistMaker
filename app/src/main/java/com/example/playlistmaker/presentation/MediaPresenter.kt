package com.example.playlistmaker.presentation

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.example.playlistmaker.domain.MediaContract
import com.example.playlistmaker.domain.api.MediaRepository

class MediaPresenter(
    private val btnPlay: ImageButton,
    private val btnPause: ImageButton,
    private val progressTime: TextView,
    private val btnFavorite: ImageButton,
    private val btnDisLike: ImageButton,
    private val previewUrl: String?,

) : MediaContract.Presenter, MediaContract {
    private var mediaPlayer: MediaPlayer? = null
    private var currentPosition: Int = 0
    private var playbackState: Int = DEFAULT
    private lateinit var progressRunnable: Runnable
    private var progressHandler = Handler(Looper.getMainLooper())

    init {
        progressRunnable()
        setupMediaPlayer()
    }

    private fun setupMediaPlayer() {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(previewUrl)
        mediaPlayer?.prepare()
    }

    override fun onFavoriteClicked() {
        btnDisLike.visibility = View.VISIBLE
    }

    override fun onDisLikeClicked() {
        btnDisLike.visibility = View.GONE
    }

    override fun onPlayClicked() {
        btnPause.visibility = View.VISIBLE
        when (playbackState) {
            DEFAULT -> {
                playAudio(previewUrl)
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

    override fun progressRunnable() {
        progressRunnable = Runnable {
            updateProgressTime(progressTime)
            progressHandler.postDelayed(progressRunnable, 1000)
        }
    }

    private fun updateProgressTime(progressTime: TextView) {
        val progress = mediaPlayer?.currentPosition
        progressTime.text = "${formatTime(progress!!)}"
    }

    private fun playAudio(audioUrl: String?) {
        try {
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                setDataSource(audioUrl)
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

    fun startAudio() {
        mediaPlayer?.start()
        playbackState = PLAYING
        progressHandler.post(progressRunnable)
    }

    fun pauseAudio() {
        mediaPlayer?.pause()
        currentPosition = mediaPlayer?.currentPosition ?: 0
        playbackState = PAUSED
        progressHandler.removeCallbacks(progressRunnable)
    }

    fun resumeAudio() {
        mediaPlayer?.seekTo(currentPosition)
        mediaPlayer?.start()
        playbackState = PLAYING
        progressHandler.post(progressRunnable)
    }

    override fun onPause() {
        btnPause.visibility = View.GONE
        if (playbackState == PLAYING) {
            pauseAudio()
        }
    }

    private fun formatTime(timeInMillis: Int): String {
        val minutes = timeInMillis / 1000 / 60
        val seconds = timeInMillis / 1000 % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        progressHandler.removeCallbacks(progressRunnable)
    }

    companion object {
        const val DEFAULT = 0
        const val PREPARED = 1
        const val PLAYING = 2
        const val PAUSED = 3
    }
}

