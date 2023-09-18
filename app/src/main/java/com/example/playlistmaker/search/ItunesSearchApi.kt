package com.example.playlistmaker.search

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesSearchApi {
    @GET("search?entity=song")
    fun search(@Query("term") term: String): Call<JsonObject>
}