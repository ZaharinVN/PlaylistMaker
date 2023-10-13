package com.example.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.search.data.HistoryRepositoryImpl
import com.example.playlistmaker.search.data.ItunesSearchApi
import com.example.playlistmaker.search.data.SearchRepositoryImpl
import com.example.playlistmaker.search.domain.HistoryRepository
import com.example.playlistmaker.search.domain.HistoryInteractor
import com.example.playlistmaker.search.domain.HistoryInteractorImpl
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.search.domain.SearchUseCase
import com.example.playlistmaker.search.domain.SearchUseCaseImpl
import com.example.playlistmaker.settings.data.SettingsInteractorImpl
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.ui.SettingsViewModelFactory
import com.example.playlistmaker.sharing.domain.SharingRepository
import com.example.playlistmaker.sharing.data.SharingRepositoryImpl
import com.example.playlistmaker.sharing.domain.SharingInteractor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val itunesSearchApi = retrofit.create(ItunesSearchApi::class.java)
    fun provideMediaPlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(provideMediaPlayerRepository(String.toString()))
    }

    private fun provideMediaPlayerRepository(audioUrl: String?): PlayerRepository {
        return PlayerRepositoryImpl(audioUrl)
    }

    fun createSharingInteractor(sharingRepository: SharingRepository): SharingInteractor {
        return SharingInteractor(sharingRepository)
    }

    fun createHistoryUseCase(historyRepository: HistoryRepository): HistoryInteractor {
        return HistoryInteractorImpl(historyRepository)
    }

    fun createHistoryRepository(sharedPreferences: SharedPreferences): HistoryRepository {
        return HistoryRepositoryImpl(sharedPreferences)
    }

    fun createSearchRepository(itunesSearchApi: ItunesSearchApi): SearchRepository {
        return SearchRepositoryImpl(itunesSearchApi)
    }

    fun createSearchUseCase(searchRepository: SearchRepository): SearchUseCase {
        return SearchUseCaseImpl(searchRepository)
    }

    fun createSettingsRepository(sharedPrefs: SharedPreferences): SettingsRepository {
        return SettingsRepositoryImpl(sharedPrefs)
    }

    fun createSharingRepository(context: Context): SharingRepository {
        return SharingRepositoryImpl(context)
    }

    fun createSettingsInteractor(
        settingsRepository: SettingsRepository,
        sharingRepository: SharingRepository
    ): SettingsInteractor {
        return SettingsInteractorImpl(settingsRepository,sharingRepository)
    }
    fun createSettingsRepository(context: Context): SettingsRepository {
        val sharedPrefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        // Создание и возвращение репозитория
        return SettingsRepositoryImpl(sharedPrefs)
    }
    fun createSettingsViewModelFactory(context: Context): SettingsViewModelFactory {
        val settingsRepository = createSettingsRepository(context)
        val sharingRepository = createSharingRepository(context)
        val settingsInteractor = createSettingsInteractor(settingsRepository,sharingRepository)
        val sharingInteractor = createSharingInteractor(sharingRepository)
        return SettingsViewModelFactory(settingsInteractor, sharingInteractor)
    }
}
