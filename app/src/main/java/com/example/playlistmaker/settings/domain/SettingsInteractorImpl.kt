package com.example.playlistmaker.settings.domain

import com.example.playlistmaker.settings.data.SettingsRepository
import com.example.playlistmaker.sharing.data.SharingRepository

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository,
    private val sharingRepository: SharingRepository
) :
    SettingsInteractor {
    override fun getDarkTheme(): Boolean {
        return settingsRepository.getDarkTheme()
    }

    override fun setDarkTheme(enabled: Boolean) {
        settingsRepository.setDarkTheme(enabled)
    }

    override fun shareApp() {
        sharingRepository.shareApp()
    }

    override fun sendSupportEmail() {
        sharingRepository.sendSupportEmail()
    }

    override fun openAgreementUrl() {
        sharingRepository.openAgreementUrl()
    }

    override fun setAppTheme() {
        settingsRepository.setAppTheme()
    }
}