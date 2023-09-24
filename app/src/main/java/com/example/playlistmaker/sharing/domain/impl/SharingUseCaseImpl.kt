package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.sharing.domain.api.SharingRepository
import com.example.playlistmaker.sharing.domain.api.SharingUseCase


class SharingUseCaseImpl(private val sharingRepository: SharingRepository) : SharingUseCase {
    override fun shareApp() {
        sharingRepository.shareApp("https://practicum.yandex.ru/android-developer/")
    }

    override fun sendSupportEmail() {
        sharingRepository.sendSupportEmail("Сообщение разработчикам и разработчицам приложения Playlist Maker", "Спасибо разработчикам и разработчицам за крутое приложение!")
    }

    override fun openAgreementUrl() {
        sharingRepository.openAgreementUrl("https://yandex.ru/legal/practicum_offer/")

    }
}