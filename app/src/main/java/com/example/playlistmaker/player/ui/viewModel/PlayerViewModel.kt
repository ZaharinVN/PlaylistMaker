package com.example.playlistmaker.player.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.db.FavoritesInteractor
import com.example.playlistmaker.library.domain.db.PlaylistDatabaseInteractor
import com.example.playlistmaker.library.domain.db.PlaylistMediaDatabaseInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.api.PlaylistTrackDatabaseInteractor
import com.example.playlistmaker.player.ui.PlayerState
import com.example.playlistmaker.player.ui.PlaylistTrackState
import com.example.playlistmaker.search.domain.model.TrackSearchModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val track: TrackSearchModel,
    private val playerInteractor: PlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistMediaDatabaseInteractor: PlaylistMediaDatabaseInteractor,
    private val playlistTrackDatabaseInteractor: PlaylistTrackDatabaseInteractor,
    private val playlistDatabaseInteractor: PlaylistDatabaseInteractor,
) : ViewModel() {

    private var timerJob: Job? = null
    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    val observePlayerState: LiveData<PlayerState> = playerState

    private val isFavorite = MutableLiveData(track.isFavorite)
    val observeIsFavorite: LiveData<Boolean> = isFavorite

    private val _checkIsTrackInPlaylist = MutableLiveData<PlaylistTrackState>()
    val checkIsTrackInPlaylist: LiveData<PlaylistTrackState> = _checkIsTrackInPlaylist

    private var _playlistsFromDatabase = MutableLiveData<List<Playlist>>()
    var playlistsFromDatabase: LiveData<List<Playlist>> = _playlistsFromDatabase

    init {
        playerInteractor.preparePlayer(
            track.previewUrl,
            { playerState.postValue(PlayerState.Prepared()) },
            { playerState.postValue(PlayerState.Prepared()) })
        playerInteractor.setOnCompletionListener {
            timerJob?.cancel()
            playerState.postValue(PlayerState.Prepared())
        }
    }

    fun playbackControl() {
        playerInteractor.playbackControl(
            startAudioPlayer(),
            pauseAudioPlayer()
        )
    }

    fun onFavoriteClicked() {
        if (isFavorite.value == false) {
            track.isFavorite = true
            viewModelScope.launch {
                favoritesInteractor.addToFavorites(track)
            }
            isFavorite.postValue(true)
        } else {
            track.isFavorite = false
            viewModelScope.launch {
                favoritesInteractor.deleteFromFavorites(track)
            }
            isFavorite.postValue(false)
        }
    }

    private fun startAudioPlayer(): () -> Unit = {
        playerInteractor.startPlayer {
            playerState.postValue(PlayerState.Playing(getCurrentPosition()))
            updateTime()
        }
    }

    private fun pauseAudioPlayer(): () -> Unit = {
        playerInteractor.pausePlayer {
            timerJob?.cancel()
            if (playerState.value is PlayerState.Playing) {
                playerState.postValue(PlayerState.Paused(getCurrentPosition()))
            }
        }
    }

    private val timeFormat: SimpleDateFormat by lazy {
        SimpleDateFormat("mm:ss", Locale.getDefault())
    }

    private fun getCurrentPosition(): String {
        return timeFormat.format(playerInteractor.getCurrentPosition())
    }

    override fun onCleared() {
        super.onCleared()
        releaseAudioPlayer()
    }

    private fun updateTime() {
        timerJob = viewModelScope.launch {
            while (playerInteractor.isPlaying()) {
                delay(PLAYBACK_UPDATE_DELAY_MS)
                playerState.postValue(PlayerState.Playing(getCurrentPosition()))
            }
        }
    }

    fun releaseAudioPlayer() {
        playerInteractor.destroyPlayer()
        playerState.value = PlayerState.Default()
    }

    fun getPlaylists() {
        viewModelScope.launch {
            playlistMediaDatabaseInteractor
                .getPlaylistsFromDatabase()
                .collect { listOfPlaylists ->
                    _playlistsFromDatabase.postValue(listOfPlaylists)
                }
        }
    }

    private fun returnPlaylistToDatabase(playlist: Playlist) {
        viewModelScope.launch {
            playlistDatabaseInteractor.insertPlaylistToDatabase(playlist)
        }
    }

    private fun convertListToString(list: List<Int>): String {
        if (list.isEmpty()) return ""
        return list.joinToString(separator = ",")
    }

    private fun convertStringToList(string: String): ArrayList<Int> {
        if (string.isEmpty()) return ArrayList<Int>()
        return ArrayList(string.split(",").map { item -> item.toInt() })
    }

    fun checkAndAddTrackToPlaylist(playlist: Playlist, track: TrackSearchModel?) {
        val listIdOfPlaylistTracks: ArrayList<Int> = convertStringToList(playlist.listOfTracksId)
        if (!playlistTrackDatabaseInteractor.isTrackInPlaylist(listIdOfPlaylistTracks, track)) {
            track?.let { listIdOfPlaylistTracks.add(it.trackId!!.toInt()) }
            val listString = convertListToString(listIdOfPlaylistTracks)
            val modifiedPlaylist: Playlist = playlist.copy(
                listOfTracksId = listString,
                amountOfTracks = playlist.amountOfTracks + 1
            )
            returnPlaylistToDatabase(modifiedPlaylist)
            track?.let { insertTrackToDatabase(it) }
            _checkIsTrackInPlaylist.postValue(
                PlaylistTrackState(
                    nameOfPlaylist = playlist.name,
                    trackIsInPlaylist = false
                )
            )
        } else {
            _checkIsTrackInPlaylist.postValue(
                PlaylistTrackState(
                    nameOfPlaylist = playlist.name,
                    trackIsInPlaylist = true
                )
            )
        }
    }

    private fun insertTrackToDatabase(track: TrackSearchModel) {
        viewModelScope.launch {
            playlistTrackDatabaseInteractor.insertTrackToPlaylistTrackDatabase(track)
        }
    }


    companion object {
        private const val PLAYBACK_UPDATE_DELAY_MS = 300L
    }
}
















