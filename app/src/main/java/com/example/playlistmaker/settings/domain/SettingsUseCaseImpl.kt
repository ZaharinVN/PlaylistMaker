package com.example.playlistmaker.settings.domain

class SettingsUseCaseImpl(private val settingsRepository: SettingsRepositoryInterface) : SettingsUseCaseInterface {
    override fun getDarkTheme(): Boolean {
        return settingsRepository.getDarkTheme()
    }

    override fun setDarkTheme(enabled: Boolean) {
        settingsRepository.setDarkTheme(enabled)
    }
}