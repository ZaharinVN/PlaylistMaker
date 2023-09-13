package com.example.playlistmaker.data

import android.media.AudioAttributes
import android.media.MediaPlayer
import com.example.playlistmaker.domain.Player

class PlayerImpl(private val audioUrl: String?) :
    Player {
    private var mediaPlayer: MediaPlayer? = null
    private var currentPosition: Int = 0
    private var playbackState: Int = DEFAULT

    init {
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
            }
        }
    }

    override fun startAudio() {
        mediaPlayer?.start()
        playbackState = PLAYING
    }

    override fun pauseAudio() {
        mediaPlayer?.pause()
        currentPosition = mediaPlayer?.currentPosition ?: 0
        playbackState = PAUSED
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    companion object {
        const val DEFAULT = 0
        const val PLAYING = 1
        const val PAUSED = 2
    }
}