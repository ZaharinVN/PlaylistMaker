package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.ItunesSearchResult

class SearchUseCaseImpl(private val searchRepository: SearchRepository) : SearchUseCase {

    override fun search(query: String, onResponse: (List<ItunesSearchResult>) -> Unit, onFailure: (Throwable) -> Unit) {
        searchRepository.search(query, onResponse, onFailure)
    }
}



