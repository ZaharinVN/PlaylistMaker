package com.example.playlistmaker.settings.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.SwitchCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.api.SettingsRepository
import com.example.playlistmaker.settings.domain.api.SettingsUseCase
import com.example.playlistmaker.sharing.domain.api.SharingUseCase


class SettingsActivity : AppCompatActivity() {
    private lateinit var switch: SwitchCompat
    private lateinit var settingsUseCase: SettingsUseCase
    private lateinit var sharingUseCase: SharingUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        switch = findViewById(R.id.themeSwitcher)
        val sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val settingsRepository: SettingsRepository = Creator.createSettingsRepository(sharedPrefs)
        settingsUseCase = Creator.createSettingsUseCase(settingsRepository)
        val sharingRepository = Creator.createSharingRepository(this)
        sharingUseCase = Creator.createSharingUseCase(sharingRepository)

        switch.isChecked = settingsUseCase.getDarkTheme()

        switch.setOnCheckedChangeListener { _, isChecked ->
            settingsUseCase.setDarkTheme(isChecked)
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
        val darkMode = settingsUseCase.getDarkTheme()
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun onShareClick(view: View?) {
        sharingUseCase.shareApp()
    }

    fun onSupportClick(view: View?) {
        sharingUseCase.sendSupportEmail()
    }

    fun onAgreementClick(view: View?) {
        sharingUseCase.openAgreementUrl()
    }
}



