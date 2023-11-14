package com.example.playlistmaker.player.ui.viewModel

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.api.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val trackUrl: String
) : ViewModel() {
    private var timerJob: Job? = null
    private var clickAllowed = true
    private val player: MediaPlayer = MediaPlayer()
    private val stateLiveData = MutableLiveData<PlayerState>()
    private val timerLiveData = MutableLiveData<String>()
    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    fun observeState(): LiveData<PlayerState> = stateLiveData
    fun observeTimer(): LiveData<String> = timerLiveData

    init {
        renderState(PlayerState.Prepared)
        preparePlayer()
        setOnCompleteListener()
        isClickAllowed()
    }

    private fun preparePlayer() {
        playerInteractor.preparePlayer(trackUrl) {
            renderState(PlayerState.Prepared)
        }
    }

    private fun startAudioPlayer() {
        playerInteractor.startPlayer()
        renderState(PlayerState.Playing(playerInteractor.getCurrentPosition()))
    }

    private fun pauseAudioPlayer() {
        playerInteractor.pausePlayer()
        renderState(PlayerState.Paused)
    }

    private fun getCurrentPosition(): Int {
        return playerInteractor.getCurrentPosition()
    }

    private fun setOnCompleteListener() {
        playerInteractor.setOnCompletionListener {
            renderState(PlayerState.Prepared)
            renderState(PlayerState.Prepared)
        }
    }

    fun playbackControl() {
        when (stateLiveData.value) {
            is PlayerState.Playing -> {
                pauseAudioPlayer()
            }
            is PlayerState.Prepared, PlayerState.Paused -> {
                startAudioPlayer()
                timerJob = viewModelScope.launch {
                    while (isActive) {
                        updateTime()
                        delay(PLAYBACK_UPDATE_DELAY_MS)
                    }
                }
            }
            else -> {}
        }
    }

    private fun renderState(state: PlayerState) {
        stateLiveData.postValue(state)
    }

    override fun onCleared() {
        super.onCleared()
        releaseAudioPlayer()
    }

    fun onPause() {
        pauseAudioPlayer()
    }

    fun onPlay() {
        playbackControl()
    }

    private val timeFormat: SimpleDateFormat by lazy {
        SimpleDateFormat("mm:ss", Locale.getDefault())
    }

    private fun updateTime() {
        timerLiveData.postValue(
            timeFormat.format(getCurrentPosition())
        )
    }

    private fun isClickAllowed() {
        if (clickAllowed) {
            clickAllowed = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MS)
                clickAllowed = true
            }
        }
    }

    private fun releaseAudioPlayer() {
        player.stop()
        player.release()
        renderState(PlayerState.Default)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MS = 2000L
        private const val PLAYBACK_UPDATE_DELAY_MS = 300L
    }
}















