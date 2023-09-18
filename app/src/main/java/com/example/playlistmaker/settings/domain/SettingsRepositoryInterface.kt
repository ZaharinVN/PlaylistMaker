package com.example.playlistmaker.settings.domain

interface SettingsRepositoryInterface {
    fun getDarkTheme(): Boolean
    fun setDarkTheme(enabled: Boolean)
}