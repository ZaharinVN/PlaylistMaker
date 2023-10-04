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
    private val _trackCoverUrl = MutableLiveData<String>()
    val trackCoverUrl: LiveData<String> = _trackCoverUrl
    private val _trackName = MutableLiveData<String>()
    val trackName: LiveData<String> = _trackName
    private val _artistName = MutableLiveData<String>()
    val artistName: LiveData<String> = _artistName
    private val _trackTime = MutableLiveData<Long>()
    val trackTime: LiveData<Long> = _trackTime
    private val _collectionName = MutableLiveData<String>()
    val collectionName: LiveData<String> = _collectionName
    private val _releaseDate = MutableLiveData<String>()
    val releaseDate: LiveData<String> = _releaseDate
    private val _primaryGenreName = MutableLiveData<String>()
    val primaryGenreName: LiveData<String> = _primaryGenreName
    private val _country = MutableLiveData<String>()
    val country: LiveData<String> = _country
    private val _progressTime = MutableLiveData<String>()
    val progressTime: LiveData<String> = _progressTime

    init {
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
        _trackCoverUrl.value = trackCoverUrl
        _trackName.value = trackName
        _artistName.value = artistName
        _trackTime.value = trackTime
        _collectionName.value = collectionName
        _releaseDate.value = releaseDate
        _primaryGenreName.value = primaryGenreName
        _country.value = country
        _progressTime.value = "00:00"
    }

    fun initialize(
        btnPlay: ImageButton,
        btnPause: ImageButton,
        progressTime: TextView,
        btnFavorite: ImageButton,
        btnDisLike: ImageButton,
        previewUrl: String?,
        interactor: PlayerInteractor
    ) {
        presenter = MediaPresenter(
            btnPlay,
            btnPause,
            progressTime,
            btnFavorite,
            btnDisLike,
            previewUrl,
            interactor
        )
        bindPresenter(presenter)
    }

    private fun bindPresenter(presenter: MediaContract.Presenter) {
        this.presenter = presenter
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














