package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.domain.ItunesSearchResult
import com.example.playlistmaker.search.domain.SearchRepository
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchRepositoryImpl(private val api: ItunesSearchApi) : SearchRepository {
    override fun search(
        query: String,
        onResponse: (List<ItunesSearchResult>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        api.search(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val resultJson = response.body()
                    val searchResults: List<ItunesSearchResult> = Gson().fromJson(
                        resultJson?.getAsJsonArray("results"),
                        object : TypeToken<List<ItunesSearchResult>>() {}.type
                    )
                    onResponse(searchResults)
                } else {
                    onFailure(Exception("Network request failed with code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onFailure(t)
            }
        })
    }
}



