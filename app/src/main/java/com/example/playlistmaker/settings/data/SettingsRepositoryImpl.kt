package com.example.playlistmaker.settings.data

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(private val sharedPrefs: SharedPreferences) : SettingsRepository {
    override fun getDarkTheme(): Boolean {
        return sharedPrefs.getBoolean("darkTheme", false)
    }

    override fun setDarkTheme(enabled: Boolean) {
        sharedPrefs.edit().putBoolean("darkTheme", enabled).apply()
    }

    override fun setAppTheme() {
        val darkMode = this.getDarkTheme()
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}