package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<Button>(R.id.btnSettingsBack)
        backButton.setOnClickListener {
            finish()
        }
    }
    fun onShareClick(view: View?) {
        val shareUrl = getString(R.string.shareUrl)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareUrl)
        startActivity(Intent.createChooser(intent, "@string/shareApp"))
    }
    fun onSupportClick(view: View?) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse(getString(R.string.sendTo))
        intent.putExtra(
            Intent.EXTRA_SUBJECT,
            getString(R.string.sendHeader))
        intent.putExtra(
            Intent.EXTRA_TEXT,
            getString(R.string.sendText))
        startActivity(Intent.createChooser(intent, getString(R.string.sendTitle)))
    }

    fun onAgreementClick(view: View?) {
        val agreementUrl = getString(R.string.agreementUrl)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(agreementUrl)
        startActivity(intent)
    }
}