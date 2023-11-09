package com.example.playlistmaker.search.ui.viewModel

import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.TrackPlayerModel
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.model.TrackSearchModel
import com.example.playlistmaker.search.ui.model.ScreenState
import com.example.playlistmaker.utils.debounce
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val app: Application,
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private var clickAllowed = true
    private val _stateLiveData = MutableLiveData<ScreenState>()
    fun stateLiveData(): LiveData<ScreenState> = _stateLiveData
    private var latestSearchText: String? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val searchTrack =
        debounce<String>(SEARCH_DEBOUNCE_DELAY_MS, viewModelScope, true) { changedText ->
            search(changedText)
        }

    fun searchDebounce(changedText: String) {
        if (latestSearchText != changedText) {
            latestSearchText = changedText
            searchTrack(changedText)
        }
    }

    private fun isClickAllowed(): Boolean {
        val current = clickAllowed
        if (clickAllowed) {
            clickAllowed = false
            coroutineScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MS)
                clickAllowed = true
            }
        }
        return current
    }

    private fun search(expression: String) {
        if (expression.isNotEmpty()) {
            renderState(ScreenState.Loading)

            searchInteractor.searchTracks(expression, object : SearchInteractor.SearchConsumer {
                override fun consume(foundTracks: List<TrackSearchModel>?, hasError: Boolean?) {
                    val tracks = mutableListOf<TrackSearchModel>()

                    if (foundTracks != null) {
                        tracks.addAll(foundTracks)

                        when {
                            tracks.isEmpty() -> {
                                renderState(ScreenState.Empty())
                            }

                            else -> {
                                renderState(ScreenState.Content(tracks))
                            }
                        }
                    } else {
                        renderState(ScreenState.Error())
                    }
                }
            })
        }
    }


    fun getTracksHistory() {
        searchInteractor.getTracksHistory(object : SearchInteractor.HistoryConsumer {
            override fun consume(tracks: List<TrackSearchModel>?) {
                if (tracks.isNullOrEmpty()) {
                    renderState(ScreenState.EmptyHistoryList())
                } else {
                    renderState(ScreenState.ContentHistoryList(tracks))
                }
            }
        })
    }

    fun onTrackClick(track: TrackSearchModel) {
        if (isClickAllowed()) {
            val trackPlayerModel = TrackPlayerModel(
                track.trackId,
                track.trackName,
                track.artistName,
                track.trackTimeMillis,
                track.artworkUrl100,
                track.collectionName,
                track.releaseDate,
                track.primaryGenreName,
                track.country,
                track.previewUrl
            )
            addTrackToHistory(track)
            val playIntent = Intent(app, PlayerActivity::class.java)
                .putExtra(EXTRA_TRACK, trackPlayerModel)
                .setFlags(FLAG_ACTIVITY_NEW_TASK)
            app.startActivity(playIntent)
        }
    }

    fun addTrackToHistory(track: TrackSearchModel) {
        searchInteractor.addTrackToHistory(track)
    }

    fun clearHistory() {
        searchInteractor.clearHistory()
    }

    private fun renderState(state: ScreenState) {
        _stateLiveData.postValue(state)
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY_MS = 2000L
        const val EXTRA_TRACK = "EXTRA_TRACK"
        const val CLICK_DEBOUNCE_DELAY_MS = 2000L
    }
}


