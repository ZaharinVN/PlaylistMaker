package com.example.playlistmaker.settings.data

interface SettingsRepository {
    fun getDarkTheme(): Boolean
    fun setDarkTheme(enabled: Boolean)
    fun setAppTheme()
}