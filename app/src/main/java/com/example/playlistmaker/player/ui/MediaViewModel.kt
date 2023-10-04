package com.example.playlistmaker.player.ui


import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.api.MediaRepository
import com.example.playlistmaker.player.domain.api.PlayerInteractor

class MediaViewModel(private val repository: MediaRepository) : ViewModel() {

    private lateinit var presenter: MediaContract.Presenter
    private val _mediaViewState = MutableLiveData<MediaViewState>()
    val mediaViewState: LiveData<MediaViewState> = _mediaViewState

    fun initialize(
        btnPlay: ImageButton,
        btnPause: ImageButton,
        progressTime: TextView,
        btnFavorite: ImageButton,
        btnDisLike: ImageButton,
        previewUrl: String?,
        interactor: PlayerInteractor
    ) {
        presenter = MediaPresenter(btnPlay, btnPause, progressTime, btnFavorite, btnDisLike, previewUrl, interactor)

        fetchData()
    }
    private fun fetchData() {
        val trackCoverUrl = repository.getTrackCoverUrl()
        val trackName = repository.getTrackName()
        val artistName = repository.getArtistName()
        val trackTime = repository.getTrackTime()?.toLong()
        val collectionName = repository.getCollectionName()
        val releaseDate = repository.getReleaseDate()
        val primaryGenreName = repository.getPrimaryGenreName()
        val country = repository.getCountry()

        val state = MediaViewState(
            trackCoverUrl,
            trackName,
            artistName,
            trackTime,
            collectionName,
            releaseDate,
            primaryGenreName,
            country,
            progressTime = "0:00"
        )
        _mediaViewState.value = state
    }

    fun onPlayClicked() {
        presenter.onPlayClicked()
    }

    fun onPauseAudioClicked() {
        presenter.onPauseAudioClicked()
    }

    fun onFavoriteClicked() {
        presenter.onFavoriteClicked()
    }

    fun onDisLikeClicked() {
        presenter.onDisLikeClicked()
    }

    fun onPause() {
        presenter.onPause()
    }

    companion object {
        fun getCoverArtwork(artworkUrl100: String): String {
            return artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
        }

        fun formatTrackDuration(duration: Long): String {
            val minutes = duration / 60000
            val seconds = (duration % 60000) / 1000
            return String.format("%02d:%02d", minutes, seconds)
        }

        fun formatReleaseDate(date: String): String {
            return date.substring(0, 4)
        }
    }
}














