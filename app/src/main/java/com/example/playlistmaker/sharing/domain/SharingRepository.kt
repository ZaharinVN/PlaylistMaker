package com.example.playlistmaker.sharing.domain

interface SharingRepository {
    fun shareApp(text: String)
    fun sendSupportEmail(subject: String, body: String)
    fun openAgreementUrl(text: String)
}
