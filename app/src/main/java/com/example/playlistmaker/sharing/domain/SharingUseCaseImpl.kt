package com.example.playlistmaker.sharing.domain

import android.content.Intent
import android.net.Uri


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