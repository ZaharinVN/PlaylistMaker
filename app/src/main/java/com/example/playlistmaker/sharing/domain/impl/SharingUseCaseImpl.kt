package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.sharing.domain.api.SharingRepository
import com.example.playlistmaker.sharing.domain.api.SharingUseCase


class SharingUseCaseImpl(private val sharingRepository: SharingRepository) : SharingUseCase {
    override fun shareApp() {
        sharingRepository.shareApp("https://practicum.yandex.ru/android-developer/")
    }

    override fun sendSupportEmail() {
        sharingRepository.sendSupportEmail("Support", "Text of the support email")
    }

    override fun openAgreementUrl() {
        sharingRepository.openAgreementUrl("https://yandex.ru/legal/practicum_offer/")

    }
}