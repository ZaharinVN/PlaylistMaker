package com.example.playlistmaker.settings.domain.api

interface SettingsRepositoryInterface {
    fun getDarkTheme(): Boolean
    fun setDarkTheme(enabled: Boolean)
}