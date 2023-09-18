package com.example.playlistmaker.creator

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.playlistmaker.player.data.PlayerImpl
import com.example.playlistmaker.player.data.MediaDataSource
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.api.MediaRepository
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsRepositoryInterface
import com.example.playlistmaker.settings.domain.SettingsUseCaseImpl
import com.example.playlistmaker.settings.domain.SettingsUseCaseInterface
import com.example.playlistmaker.sharing.domain.SharingRepository
import com.example.playlistmaker.sharing.data.SharingRepositoryImpl
import com.example.playlistmaker.sharing.domain.SharingUseCase
import com.example.playlistmaker.sharing.domain.SharingUseCaseImpl


object Creator {

    fun createMediaRepository(intent: Intent): MediaRepository {
        return MediaDataSource(intent)
    }
    fun createInteractor(audioUrl: String?): PlayerInteractor {
        val player = PlayerImpl(audioUrl)
        return PlayerInteractorImpl(player)
    }
    fun createSettingsRepository(sharedPrefs: SharedPreferences): SettingsRepositoryInterface {
        return SettingsRepositoryImpl(sharedPrefs)
    }

    fun createSettingsUseCase(settingsRepository: SettingsRepositoryInterface): SettingsUseCaseInterface {
        return SettingsUseCaseImpl(settingsRepository)
    }

    fun createSharingRepository(context: Context): SharingRepository {
        return SharingRepositoryImpl(context)
    }

    fun createSharingUseCase(sharingRepository: SharingRepository): SharingUseCase {
        return SharingUseCaseImpl(sharingRepository)
    }
}
