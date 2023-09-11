package com.example.playlistmaker.data

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.example.playlistmaker.domain.Player

class PlayerImpl(private val audioUrl: String?, private val progressTime: TextView) :
    Player {
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var progressRunnable: Runnable
    private val progressHandler = Handler(Looper.getMainLooper())
    private var currentPosition: Int = 0
    private var playbackState: Int = DEFAULT

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
                playbackState = DEFAULT
                progressTime.text = "00:00"
                progressHandler.removeCallbacks(progressRunnable)
            }
        }
    }

    override fun startAudio() {
        mediaPlayer?.start()
        progressHandler.post(progressRunnable)
        playbackState = PLAYING
    }

    override fun pauseAudio() {
        mediaPlayer?.pause()
        currentPosition = mediaPlayer?.currentPosition ?: 0
        playbackState = PAUSED
        progressHandler.removeCallbacks(progressRunnable)
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    override fun updateProgressTime() {
        val progress = mediaPlayer?.currentPosition
        progressTime.text = formatTime(progress!!)
    }

    private fun progressRunnable() {
        progressRunnable = Runnable {
            updateProgressTime()
            progressHandler.postDelayed(progressRunnable, 1000)
        }
    }

    private fun formatTime(timeInMillis: Int): String {
        val minutes = timeInMillis / 1000 / 60
        val seconds = timeInMillis / 1000 % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    companion object {
        const val DEFAULT = 0
        const val PLAYING = 1
        const val PAUSED = 2
    }
}