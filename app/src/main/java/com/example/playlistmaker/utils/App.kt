package com.example.playlistmaker.utils

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    private var darkTheme = false
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
        initTheme()
    }

    private fun initTheme() {
        sharedPref = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)

        if (sharedPref.contains(THEME_SWITCH_KEY)) {
            darkTheme = sharedPref.getBoolean(THEME_SWITCH_KEY, false)
            switchTheme(darkTheme)
        } else {
            darkTheme = false
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
