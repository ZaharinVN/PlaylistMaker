package com.example.playlistmaker.library.ui.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.db.FavoritesInteractor
import com.example.playlistmaker.library.ui.history.FavoritesState
import com.example.playlistmaker.search.data.dto.TrackDto
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val context: Context,
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {

    private val _stateLiveData = MutableLiveData<FavoritesState>()
    private val stateLiveData: LiveData<FavoritesState> = _stateLiveData

    init {
        fillData()
    }

    fun observeState(): LiveData<FavoritesState> = stateLiveData

    fun fillData() {
        renderState(FavoritesState.Loading)
        viewModelScope.launch {
            favoritesInteractor
                .favoritesTracks()
                .collect { tracks ->
                    processResult(tracks)
                }
        }
    }

    private fun processResult(tracks: List<TrackDto>) {
        if (tracks.isEmpty()) {
            renderState(FavoritesState.Empty(R.id.favorites_empty))
        } else {
            renderState(FavoritesState.Content(R.id.favorites_list))
        }
    }

    private fun renderState(state: FavoritesState) {
        _stateLiveData.postValue(state)
    }
}
