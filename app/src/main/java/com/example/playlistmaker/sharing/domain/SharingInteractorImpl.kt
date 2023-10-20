package com.example.playlistmaker.sharing.domain

import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.SharingRepository

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