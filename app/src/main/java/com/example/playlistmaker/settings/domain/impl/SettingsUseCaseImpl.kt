package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.api.SettingsRepository
import com.example.playlistmaker.settings.domain.api.SettingsUseCase

class SettingsUseCaseImpl(private val settingsRepository: SettingsRepository) :
    SettingsUseCase {
    override fun getDarkTheme(): Boolean {
        return settingsRepository.getDarkTheme()
    }

    override fun setDarkTheme(enabled: Boolean) {
        settingsRepository.setDarkTheme(enabled)
    }
}