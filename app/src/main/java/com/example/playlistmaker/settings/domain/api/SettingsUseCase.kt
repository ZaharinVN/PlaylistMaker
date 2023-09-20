package com.example.playlistmaker.settings.domain.api

interface SettingsUseCase {
    fun getDarkTheme(): Boolean
    fun setDarkTheme(enabled: Boolean)
}