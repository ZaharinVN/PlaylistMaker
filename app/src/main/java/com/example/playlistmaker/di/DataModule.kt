package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.search.data.SearchDataStorage
import com.example.playlistmaker.search.data.network.ITunesSearchApi
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.sharedPrefs.SharedPrefsSearchDataStorage
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.api.SettingsRepository
import com.example.playlistmaker.utils.ITUNES_BASE_URL
import com.example.playlistmaker.utils.SHARED_PREFERENCES
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    factory {
        MediaPlayer()
    }

    single {
        androidContext().getSharedPreferences(
            SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(sharedPrefs = get())
    }

    single<ITunesSearchApi> {
        Retrofit.Builder()
            .baseUrl(ITUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesSearchApi::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(iTunesService = get(), androidContext())
    }

    single<SearchDataStorage> {
        SharedPrefsSearchDataStorage(sharedPref = get(), gson = get())
    }

    factory { Gson() }
}