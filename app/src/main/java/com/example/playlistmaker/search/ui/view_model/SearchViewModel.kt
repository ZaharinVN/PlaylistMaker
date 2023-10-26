package com.example.playlistmaker.search.ui.view_model

import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.TrackPlayerModel
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.model.TrackSearchModel
import com.example.playlistmaker.search.ui.model.ScreenState

class SearchViewModel(
    private val app: Application,
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())
    private var clickAllowed = true
    private val _stateLiveData = MutableLiveData<ScreenState>()
    fun stateLiveData(): LiveData<ScreenState> = _stateLiveData
    private var latestSearchText: String? = null

    fun searchDebounce(changedText: String, hasError: Boolean) {
        if (latestSearchText == changedText && !hasError) {
            return
        }

        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val searchRunnable = Runnable { search(changedText) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY_MS
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime
        )
    }

    private fun isClickAllowed(): Boolean {
        val current = clickAllowed
        if (clickAllowed) {
            clickAllowed = false
            handler.postDelayed({ clickAllowed = true }, CLICK_DEBOUNCE_DELAY_MS)
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
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY_MS = 2000L
        val SEARCH_REQUEST_TOKEN = Any()
        const val EXTRA_TRACK = "EXTRA_TRACK"
        const val CLICK_DEBOUNCE_DELAY_MS = 2000L
    }
}


