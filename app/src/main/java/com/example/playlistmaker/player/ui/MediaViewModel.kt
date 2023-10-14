package com.example.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.domain.api.MediaPlayerState
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import java.text.SimpleDateFormat
import java.util.Locale

class MediaViewModel(
    private val mediaPlayerInteractor: PlayerInteractor,
    private val trackUrl: String
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())
    private var clickAllowed = true

    private val stateLiveData = MutableLiveData<MediaPlayerState>()
    private val timerLiveData = MutableLiveData<String>()
    fun observeState(): LiveData<MediaPlayerState> = stateLiveData
    fun observeTimer(): LiveData<String> = timerLiveData

    init {
        renderState(MediaPlayerState.Default)
        preparePlayer()
        setOnCompleteListener()
        isClickAllowed()
    }

    private fun preparePlayer() {
        mediaPlayerInteractor.preparePlayer(trackUrl) {
            renderState(MediaPlayerState.Prepared)
        }
    }

    private fun startAudioPlayer() {
        mediaPlayerInteractor.startAudio()
        renderState(MediaPlayerState.Playing(mediaPlayerInteractor.currentPosition()))
    }

    private fun pauseAudioPlayer() {
        mediaPlayerInteractor.pauseAudio()
        renderState(MediaPlayerState.Paused)
    }

    private fun getCurrentPosition(): Int {
        return mediaPlayerInteractor.currentPosition()
    }

    private fun setOnCompleteListener() {
        mediaPlayerInteractor.setOnCompletionListener {
            renderState(MediaPlayerState.Prepared)
        }
    }

    fun playbackControl() {
        when (stateLiveData.value) {
            is MediaPlayerState.Playing -> {
                pauseAudioPlayer()
            }

            is MediaPlayerState.Prepared, MediaPlayerState.Paused -> {
                startAudioPlayer()
                handler.post(updateTime())
            }

            else -> {}
        }
    }

    private fun renderState(state: MediaPlayerState) {
        stateLiveData.postValue(state)
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(null)
        mediaPlayerInteractor.destroyPlayer()
    }

    fun onPause() {
        pauseAudioPlayer()
        handler.removeCallbacksAndMessages(updateTime())
    }

    private fun updateTime(): Runnable {
        return object : Runnable {
            override fun run() {
                timerLiveData.postValue(
                    SimpleDateFormat("mm:ss", Locale.getDefault())
                        .format(getCurrentPosition())
                )
                handler.postDelayed(this, PLAYBACK_UPDATE_DELAY_MS)
            }
        }
    }

    fun isClickAllowed(): Boolean {
        val current = clickAllowed
        if (clickAllowed) {
            clickAllowed = false
            handler.postDelayed({ clickAllowed = true }, CLICK_DEBOUNCE_DELAY_MS)
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MS = 2000L
        private const val PLAYBACK_UPDATE_DELAY_MS = 300L

        fun getViewModelFactory(url: String): ViewModelProvider.Factory = viewModelFactory() {
            initializer {
                MediaViewModel(Creator.provideMediaPlayerInteractor(), url)
            }
        }
    }

}














