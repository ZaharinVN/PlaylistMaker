package com.example.playlistmaker.player.data

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.player.domain.api.Player

class PlayerImpl(private val audioUrl: String?) : Player {

    private var mediaPlayer: MediaPlayer? = null
    private var currentPosition: Int = 0
    private var playbackState: PlayerState = PlayerState.DEFAULT
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    override fun startAudio() {
        mediaPlayer?.start()
        playbackState = PlayerState.PLAYING
    }

    override fun pauseAudio() {
        mediaPlayer?.pause()
        currentPosition = mediaPlayer?.currentPosition ?: 0
        playbackState = PlayerState.PAUSED
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    override fun currentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    override fun preparePlayer(
        dataSource: String,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit
    ) {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            setDataSource(audioUrl)
            prepareAsync()
            setOnPreparedListener {
                onPreparedListener()
                playbackState = PlayerState.PREPARED
            }
            setOnCompletionListener {
                onCompletionListener()
                playbackState = PlayerState.DEFAULT
            }
        }
    }

    override fun playbackControl(onStartPlayer: () -> Unit, onPausePlayer: () -> Unit) {
        when (playbackState) {
            PlayerState.PLAYING -> {
                onPausePlayer()
                pauseAudio()
                playbackState = PlayerState.PAUSED
            }

            PlayerState.PREPARED, PlayerState.PAUSED -> {
                onStartPlayer()
                startAudio()
                playbackState = PlayerState.PLAYING
            }

            PlayerState.DEFAULT -> {}
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    enum class PlayerState {
        DEFAULT, PREPARED, PLAYING, PAUSED
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}