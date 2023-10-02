package com.example.playlistmaker.settings.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator

class SettingsActivity : AppCompatActivity() {
    private lateinit var switch: SwitchCompat
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        switch = findViewById(R.id.themeSwitcher)

        val sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val settingsRepository = Creator.createSettingsRepository(sharedPrefs)
        val sharingRepository = Creator.createSharingRepository(this)
        val settingsUseCase = Creator.createSettingsUseCase(settingsRepository)
        val sharingUseCase = Creator.createSharingUseCase(sharingRepository)
        viewModel =
            ViewModelProvider(this, SettingsViewModelFactory(settingsUseCase, sharingUseCase)).get(
                SettingsViewModel::class.java
            )
        viewModel.getDarkThemeLiveData().observe(this) { isDarkTheme ->
            switch.isChecked = isDarkTheme
            setAppTheme()
        }

        switch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setDarkTheme(isChecked)
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
        val darkMode = viewModel.getDarkTheme()
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun onShareClick() {
        viewModel.shareApp()
    }

    fun onSupportClick() {
        viewModel.sendSupportEmail()
    }

    fun onAgreementClick() {
        viewModel.openAgreementUrl()
    }
}





