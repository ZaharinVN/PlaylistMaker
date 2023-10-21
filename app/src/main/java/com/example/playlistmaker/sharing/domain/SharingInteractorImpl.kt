package com.example.playlistmaker.sharing.domain

import com.example.playlistmaker.sharing.data.SharingRepository

class SharingInteractorImpl(
    private val sharingRepository: SharingRepository
) : SharingInteractor {
    override fun shareApp() {
        sharingRepository.shareApp()
    }

    override fun sendSupportEmail() {
        sharingRepository.sendSupportEmail()
    }

    override fun openAgreementUrl() {
        sharingRepository.openAgreementUrl()
    }
}