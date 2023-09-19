package com.example.playlistmaker.sharing.domain.api

interface SharingUseCase {
    fun shareApp()
    fun sendSupportEmail()
    fun openAgreementUrl()
}