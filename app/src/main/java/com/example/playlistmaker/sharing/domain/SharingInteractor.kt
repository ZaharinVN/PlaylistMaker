package com.example.playlistmaker.sharing.domain

class SharingInteractor(
    private val sharingRepository:
    SharingRepository
) {
    fun shareApp() {
        sharingRepository.shareApp()
    }

    fun sendSupportEmail() {
        sharingRepository.sendSupportEmail()
    }

    fun openAgreementUrl() {
        sharingRepository.openAgreementUrl()
    }
}