package com.example.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.sharing.domain.SharingRepository

class SharingRepositoryImpl(private val context: Context) : SharingRepository {
    override fun shareApp(text: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(Intent.createChooser(intent, "https://practicum.yandex.ru/android-developer/"))
    }

    override fun sendSupportEmail(subject: String, body: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, body)
        context.startActivity(Intent.createChooser(intent, "Send Email"))
    }
    override fun openAgreementUrl(text: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://yandex.ru/legal/practicum_offer/")
        context.startActivity(Intent.createChooser(intent, "https://yandex.ru/legal/practicum_offer/"))
    }
}

