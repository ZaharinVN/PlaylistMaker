package com.example.playlistmaker.search.domain

class SearchUseCaseImpl(private val searchRepository: SearchRepository) : SearchUseCase {

    override fun search(
        query: String,
        onResponse: (List<ItunesSearchResult>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        searchRepository.search(query, onResponse, onFailure)
    }
}



