package com.example.playlistmaker.settings.domain

interface SettingsUseCaseInterface {
    fun getDarkTheme(): Boolean
    fun setDarkTheme(enabled: Boolean)
}