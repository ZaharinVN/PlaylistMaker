package com.example.playlistmaker.sharing.domain

interface SharingUseCase {
    fun shareApp()
    fun sendSupportEmail()
    fun openAgreementUrl()
}