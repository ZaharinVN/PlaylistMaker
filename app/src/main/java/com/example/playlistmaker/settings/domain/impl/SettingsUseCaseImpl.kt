package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.api.SettingsRepositoryInterface
import com.example.playlistmaker.settings.domain.api.SettingsUseCaseInterface

class SettingsUseCaseImpl(private val settingsRepository: SettingsRepositoryInterface) :
    SettingsUseCaseInterface {
    override fun getDarkTheme(): Boolean {
        return settingsRepository.getDarkTheme()
    }

    override fun setDarkTheme(enabled: Boolean) {
        settingsRepository.setDarkTheme(enabled)
    }
}