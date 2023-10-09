package com.example.playlistmaker.settings.domain


interface SettingsInteractor {
    fun getDarkTheme(): Boolean
    fun setDarkTheme(enabled: Boolean)
    fun shareApp()
    fun sendSupportEmail()
    fun openAgreementUrl()
    fun setAppTheme()
}