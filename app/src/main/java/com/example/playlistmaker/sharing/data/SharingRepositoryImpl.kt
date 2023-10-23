package com.example.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.res.TypedArrayUtils.getString
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.api.SharingRepository
import java.lang.Exception

class SharingRepositoryImpl(
    private val context: Context
) : SharingRepository {
    override fun shareApp() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.shareApp))
        }
        context.startActivity(shareIntent)
    }

    override fun sendSupportEmail() {
        val feedbackIntent = Intent().apply {
            action = Intent.ACTION_SENDTO
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.sendToEmail)))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.sendHeader))
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.sendText))
        }
        try {
            context.startActivity(feedbackIntent)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                context.getString(R.string.mail_app_not_found),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun openAgreementUrl() {
        val licenseIntent = Intent().apply {
            action = Intent.ACTION_VIEW
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            data = Uri.parse(context.getString(R.string.agreementUrl))
        }
        context.startActivity(licenseIntent)
    }
}
