package com.example.playlistmaker.data

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Handler
import android.view.View
import android.widget.ImageButton
import android.widget.TextView

class PlayerImpl(
    private val audioUrl: String?,
    private val progressTime: TextView,
    private val btnPause: ImageButton,
) {
    private var mediaPlayer: MediaPlayer? = null
    private var currentPosition: Int = 0
    private var playbackState: Int = DEFAULT
    private lateinit var progressRunnable: Runnable
    private val progressHandler = Handler()

    init {
        progressRunnable()
        setupMediaPlayer()
    }

    private fun setupMediaPlayer() {
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
    }

    fun playOrResume() {
        when (playbackState) {
            DEFAULT, PREPARED, PAUSED -> {
                mediaPlayer?.start()
                playbackState = PLAYING
                progressHandler.post(progressRunnable)
            }

            PLAYING -> {
                mediaPlayer?.pause()
                currentPosition = mediaPlayer?.currentPosition ?: 0
                playbackState = PAUSED
                progressHandler.removeCallbacks(progressRunnable)
            }
        }
    }

    private fun progressRunnable() {
        progressRunnable = Runnable {
            updateProgressTime()
            progressHandler.postDelayed(progressRunnable, 1000)
        }
    }

    private fun updateProgressTime() {
        val progress = mediaPlayer?.currentPosition
        progressTime.text = "${formatTime(progress!!)}"
    }

    fun formatTime(timeInMillis: Int): String {
        val minutes = timeInMillis / 1000 / 60
        val seconds = timeInMillis / 1000 % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun startAudio() {
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

    companion object {
        const val DEFAULT = 0
        const val PREPARED = 1
        const val PLAYING = 2
        const val PAUSED = 3
    }
}