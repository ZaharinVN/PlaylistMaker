package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.domain.SearchRepository
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRepositoryImpl(private val api: ItunesSearchApi) : SearchRepository {

    override fun search(query: String, onResponse: (List<ItunesSearchResult>) -> Unit, onFailure: (Throwable) -> Unit) {
        val call = api.search(query)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val resultJson = response.body()
                val searchResults: List<ItunesSearchResult> = Gson().fromJson(
                    resultJson?.getAsJsonArray("results"),
                    object : TypeToken<List<ItunesSearchResult>>() {}.type
                )
                onResponse(searchResults)
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onFailure(t)
            }
        })
    }
}



