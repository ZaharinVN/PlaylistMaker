package com.example.playlistmaker.library.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.container.PlaylistInfoContainer
import com.example.playlistmaker.library.domain.db.CurrentPlaylistInteractor
import com.example.playlistmaker.library.domain.db.PlaylistDatabaseInteractor
import com.example.playlistmaker.library.domain.db.PlaylistMediaDatabaseInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.player.domain.api.PlaylistTrackDatabaseInteractor

import com.example.playlistmaker.search.domain.model.TrackSearchModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlaylistInfoViewModel(

    private val currentPlaylistInteractor: CurrentPlaylistInteractor,
    private val playlistDatabaseInteractor: PlaylistDatabaseInteractor,
    private val playlistTrackDatabaseInteractor: PlaylistTrackDatabaseInteractor,
    private val playlistMediaDatabaseInteractor: PlaylistMediaDatabaseInteractor

) : ViewModel() {

    var updatedPlaylist: Playlist? = null

    val listOfCurrentTracks = ArrayList<TrackSearchModel>()

    private val _tracksForCurrentPlaylist = MutableLiveData<PlaylistInfoContainer>()
    val tracksForCurrentPlaylist: LiveData<PlaylistInfoContainer> = _tracksForCurrentPlaylist

    fun getTracksFromDatabaseForCurrentPlaylist(ids: List<Int>) {
        viewModelScope.launch {
            currentPlaylistInteractor
                .getTracksForCurrentPlaylist(ids)
                .collect { tracksForCurrentPlaylist ->
                    val listOfTotalTime = tracksForCurrentPlaylist.map { track ->
                        val timeComponents = track.trackTimeMillis?.split(":") // Разделим время на минуты и секунды
                        val minutes = timeComponents?.get(0)?.toIntOrNull() ?: 0 // Преобразуем минуты в целое число
                        val seconds = timeComponents?.get(1)?.toIntOrNull() ?: 0 // Преобразуем секунды в целое число
                        val totalMilliseconds = minutes * 60 * 1000 + seconds * 1000 // Переводим время в миллисекунды
                        totalMilliseconds
                    }

                    val sum = listOfTotalTime.sum()
                    val sumInMinutes = sum / (60 * 1000)

                    val reversedIds = ids.reversed()

                    val sortedTracks = tracksForCurrentPlaylist
                        .sortedBy { track -> reversedIds.indexOf(track.trackId.toInt()) }

                    _tracksForCurrentPlaylist.postValue(
                        PlaylistInfoContainer(
                            totalTime = pluralizeWord(sumInMinutes, "минута"),
                            playlistTracks = sortedTracks
                        )
                    )
                }
        }
    }

    fun checkAndDeleteTrackFromPlaylistTrackDatabase(track: TrackSearchModel) {
        viewModelScope.launch {
            playlistMediaDatabaseInteractor
                .getPlaylistsFromDatabase()
                .collect { listOfPlaylists ->
                    for (playlist in listOfPlaylists) {
                        val listsOfTrackIds = convertStringToList(playlist.listOfTracksId)
                        if (listsOfTrackIds.contains(track.trackId.toInt())) {
                            return@collect
                        }
                    }

                    deletePlaylistTrackFromDatabaseById(track.trackId.toInt())

                }
        }
    }

    fun updatePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistDatabaseInteractor.insertPlaylistToDatabase(playlist)
        }
    }

    fun deletePlaylistTrackFromDatabaseById(id: Int) {
        viewModelScope.launch {
            playlistTrackDatabaseInteractor.deletePlaylistTrackFromDatabaseById(id)
        }
    }

    fun deleteTrackFromPlaylistTrackDatabase(track: TrackSearchModel) {
        viewModelScope.launch {
            playlistTrackDatabaseInteractor.deletePlaylistTrackFromDatabase(track)
        }
    }

    fun getYearFromPlaylist(millis: Long?): String {
        return if (millis != null) {
            val dateFormat = SimpleDateFormat("yyyy")
            dateFormat.format(Date(millis))
        } else {
            ""
        }
    }

    fun convertListToString(list: List<Int>): String {
        if (list.isEmpty()) return ""

        return list.joinToString(separator = ",")
    }

    fun convertStringToList(string: String): ArrayList<Int> {
        if (string.isEmpty()) return ArrayList<Int>()

        return ArrayList<Int>(string.split(",").map { item -> item.toInt() })
    }

    fun getMessageForExternalResources(playlist: Playlist?): String {
        var count = 0

        var message = ""

        val nameOfPlaylist = playlist?.name ?: ""
        val description =
            if (playlist?.description.isNullOrEmpty()) "without description" else playlist?.description
        val amountOfTracks = playlist?.amountOfTracks?.let { pluralizeWord(it, "трек") } ?: ""

        message += nameOfPlaylist + "\n" + description + "\n" + amountOfTracks + "\n"

        listOfCurrentTracks.forEach { track ->
            count++

            var formattedTime =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis?.toLong())

            var trackString = "$count ${track.artistName} - ${track.trackName} ($formattedTime) \n"

            message += trackString
        }

        return message
    }

    fun deletePlaylist(playlist: Playlist?) {
        if (playlist != null) {
            viewModelScope.launch {
                playlistMediaDatabaseInteractor.deletePlaylist(playlist)
            }
        }
    }

    fun pluralizeWord(number: Int, word: String): String {
        return when {
            number % 10 == 1 && number % 100 != 11 -> "$number $word"
            number % 10 in 2..4 && (number % 100 < 10 || number % 100 >= 20) ->
                if (word.endsWith('а')) "$number ${word.dropLast(1)}ы" else "$number ${word}а"

            else ->
                if (word.endsWith('а')) "$number ${word.dropLast(1)}" else "$number ${word}ов"
        }
    }

}