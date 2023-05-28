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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    fun onShareClick(view: View?) {
        val share_url = getString(R.string.share_url)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, share_url)
        startActivity(Intent.createChooser(intent, "Поделиться приложением"))
    }
    fun onSupportClick(view: View?) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:vitalikz85@yandex.ru")
        intent.putExtra(
            Intent.EXTRA_SUBJECT,
            "Сообщение разработчикам и разработчицам приложения Playlist Maker")
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Спасибо разработчикам и разработчицам за крутое приложение!")
        startActivity(Intent.createChooser(intent, "Send email"))
    }
    fun onAgreementClick(view: View?) {
        val agreement_url = getString(R.string.agreement_url)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(agreement_url)
        startActivity(intent)
    }
}