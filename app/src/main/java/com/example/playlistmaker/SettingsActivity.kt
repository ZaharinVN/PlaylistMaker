package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {

    private lateinit var switch: SwitchMaterial
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        switch = findViewById(R.id.themeSwitcher)
        sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        switch.isChecked = sharedPrefs.getBoolean("darkMode", false)

        switch.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.edit().putBoolean("darkMode", isChecked).apply()
            setAppTheme()
        }

        val backButton = findViewById<Button>(R.id.btnSettingsBack)
        backButton.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        setAppTheme()
    }

    private fun setAppTheme() {
        val darkMode = sharedPrefs.getBoolean("darkMode", false)
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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
        intent.data = Uri.parse(getString(R.string.sendTo) + "?subject=${getString(R.string.sendHeader)}&body=${getString(R.string.sendText)}")
        startActivity(Intent.createChooser(intent, getString(R.string.sendTitle)))
    }

    fun onAgreementClick(view: View?) {
        val agreementUrl = getString(R.string.agreementUrl)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(agreementUrl)
        startActivity(intent)
    }
}
