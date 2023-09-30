package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.ItunesSearchResult

interface SearchRepository {
    fun search(
        query: String,
        onResponse: (List<ItunesSearchResult>) -> Unit,
        onFailure: (Throwable) -> Unit
    )
}
