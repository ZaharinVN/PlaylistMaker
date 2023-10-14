package com.example.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.SharingRepository

class SharingRepositoryImpl(
    private val context: Context
) : SharingRepository {
    override fun shareApp() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        context.startActivity(Intent.createChooser(intent, (R.string.shareApp.toString())))
    }

    override fun sendSupportEmail() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse(R.string.sendTo.toString())
        context.startActivity(Intent.createChooser(intent, (R.string.sendTitle.toString())))
    }

    override fun openAgreementUrl() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse((R.string.agreementUrl).toString())
        context.startActivity(Intent.createChooser(intent, (R.string.agreementUrl).toString()))
    }
}

