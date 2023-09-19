package com.example.playlistmaker.settings.domain.api

interface SettingsUseCaseInterface {
    fun getDarkTheme(): Boolean
    fun setDarkTheme(enabled: Boolean)
}