package com.example.playlistmaker.settings.data

import android.content.SharedPreferences
import com.example.playlistmaker.settings.domain.api.SettingsRepositoryInterface

class SettingsRepositoryImpl(private val sharedPrefs: SharedPreferences) :
    SettingsRepositoryInterface {
    override fun getDarkTheme(): Boolean {
        return sharedPrefs.getBoolean("darkTheme", false)
    }

    override fun setDarkTheme(enabled: Boolean) {
        sharedPrefs.edit().putBoolean("darkTheme", enabled).apply()
    }
}