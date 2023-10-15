package com.example.playlistmaker.creator

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.search.data.SearchRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.example.playlistmaker.settings.data.SettingsInteractorImpl
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.ui.SettingsViewModelFactory
import com.example.playlistmaker.sharing.domain.SharingRepository
import com.example.playlistmaker.sharing.data.SharingRepositoryImpl
import com.example.playlistmaker.sharing.domain.SharingInteractor

object Creator {

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(providePlayerRepository(MediaPlayer()))
    }

    fun providePlayerRepository(Player: MediaPlayer): PlayerRepository {
        return PlayerRepositoryImpl(Player)
    }

    fun createSharingInteractor(sharingRepository: SharingRepository): SharingInteractor {
        return SharingInteractor(sharingRepository)
    }

    fun createSharingRepository(context: Context): SharingRepository {
        return SharingRepositoryImpl(context)
    }

    fun createSettingsInteractor(
        settingsRepository: SettingsRepository,
        sharingRepository: SharingRepository
    ): SettingsInteractor {
        return SettingsInteractorImpl(settingsRepository, sharingRepository)
    }

    fun createSettingsRepository(context: Context): SettingsRepository {
        val sharedPrefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return SettingsRepositoryImpl(sharedPrefs)
    }

    fun createSettingsViewModelFactory(context: Context): SettingsViewModelFactory {
        val settingsRepository = createSettingsRepository(context)
        val sharingRepository = createSharingRepository(context)
        val settingsInteractor = createSettingsInteractor(settingsRepository, sharingRepository)
        val sharingInteractor = createSharingInteractor(sharingRepository)
        return SettingsViewModelFactory(settingsInteractor, sharingInteractor)
    }

    fun provideSearchRepository(context: Context): SearchRepository {
        return SearchRepositoryImpl(
            RetrofitNetworkClient(context),
            com.example.playlistmaker.search.data.sharedPrefs.SharedPrefsSearchDataStorage(context),
        )
    }

    fun provideSearchInteractor(context: Context): SearchInteractor {
        return SearchInteractorImpl(provideSearchRepository(context))
    }
}
