package com.example.playlistmaker.creator

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.playlistmaker.player.data.PlayerImpl
import com.example.playlistmaker.player.data.MediaDataSource
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.api.MediaRepository
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.search.data.HistoryRepositoryImpl
import com.example.playlistmaker.search.data.ItunesSearchApi
import com.example.playlistmaker.search.data.SearchRepositoryImpl
import com.example.playlistmaker.search.domain.HistoryRepository
import com.example.playlistmaker.search.domain.HistoryUseCase
import com.example.playlistmaker.search.domain.HistoryUseCaseImpl
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.search.domain.SearchUseCase
import com.example.playlistmaker.search.domain.SearchUseCaseImpl
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.api.SettingsRepository
import com.example.playlistmaker.settings.domain.impl.SettingsUseCaseImpl
import com.example.playlistmaker.settings.domain.api.SettingsUseCase
import com.example.playlistmaker.sharing.domain.api.SharingRepository
import com.example.playlistmaker.sharing.data.SharingRepositoryImpl
import com.example.playlistmaker.sharing.domain.api.SharingUseCase
import com.example.playlistmaker.sharing.domain.impl.SharingUseCaseImpl


object Creator {

    fun createMediaRepository(intent: Intent): MediaRepository {
        return MediaDataSource(intent)
    }
    fun createInteractor(audioUrl: String?): PlayerInteractor {
        val player = PlayerImpl(audioUrl)
        return PlayerInteractorImpl(player)
    }
    fun createSettingsRepository(sharedPrefs: SharedPreferences): SettingsRepository {
        return SettingsRepositoryImpl(sharedPrefs)
    }

    fun createSettingsUseCase(settingsRepository: SettingsRepository): SettingsUseCase {
        return SettingsUseCaseImpl(settingsRepository)
    }

    fun createSharingRepository(context: Context): SharingRepository {
        return SharingRepositoryImpl(context)
    }

    fun createSharingUseCase(sharingRepository: SharingRepository): SharingUseCase {
        return SharingUseCaseImpl(sharingRepository)
    }
    fun createHistoryUseCase(historyRepository: HistoryRepository): HistoryUseCase {
        return HistoryUseCaseImpl(historyRepository)
    }
    fun createHistoryRepository(sharedPreferences: SharedPreferences): HistoryRepository {
        return HistoryRepositoryImpl(sharedPreferences)
    }
    fun createSearchRepository(api: ItunesSearchApi): SearchRepository {
        return SearchRepositoryImpl(api)
    }
    fun createSearchUseCase(searchRepository: SearchRepository): SearchUseCase {
        return SearchUseCaseImpl(searchRepository)
    }
}
