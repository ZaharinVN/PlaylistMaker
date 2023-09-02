package com.example.playlistmaker.presentation

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
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
) : MediaContract.Presenter {

    private var mediaPlayer: MediaPlayer? = null
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
        btnPlay.setOnClickListener {
            btnPause.visibility = View.VISIBLE
            when (playbackState) {
                DEFAULT -> {
                    val previewUrl = (SearchActivity.EXTRA_PREVIEW)
                    playAudio(previewUrl)
                }

                PREPARED -> {
                    resumeAudio()
                    btnPause.visibility = View.GONE
                }

                PLAYING -> {
                    val previewUrl = (SearchActivity.EXTRA_PREVIEW)
                    playAudio(previewUrl)
                    btnPause.visibility = View.VISIBLE
                }

                PAUSED -> {
                    resumeAudio()

                }
            }
        }
    }

    override fun onPauseAudioClicked() {
        btnPause.setOnClickListener {
            btnPause.visibility = View.GONE
            pauseAudio()
        }
    }

    override fun onFavoriteClicked() {
        btnFavorite.setOnClickListener {
            btnDisLike.visibility = View.VISIBLE
        }
    }

    override fun onDisLikeClicked() {
        btnDisLike.setOnClickListener {
            btnDisLike.visibility = View.GONE
        }
    }

    override fun progressRunnable() {
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

    private fun updateProgressTime() {
        val progress = mediaPlayer?.currentPosition
        progressTime.text = "${formatTime(progress!!)}"
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        progressHandler.removeCallbacks(progressRunnable)
    }

}