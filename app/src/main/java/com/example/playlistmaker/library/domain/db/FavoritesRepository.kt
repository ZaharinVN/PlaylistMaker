package com.example.playlistmaker.library.domain.db

import com.example.playlistmaker.search.data.dto.TrackDto
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    fun favoritesTracks(): Flow<List<TrackDto>>
}