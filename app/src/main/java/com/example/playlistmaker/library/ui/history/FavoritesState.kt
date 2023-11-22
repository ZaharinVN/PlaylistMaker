package com.example.playlistmaker.library.ui.history

sealed interface FavoritesState {
    object Loading : FavoritesState

    data class Content(
        val container: Int
    ) : FavoritesState

    data class Empty(
        val container: Int
    ) : FavoritesState
}