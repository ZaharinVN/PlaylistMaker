package com.example.playlistmaker.settings.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.sharing.domain.api.SharingInteractor

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {
    private val darkThemeLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun setDarkTheme(isDarkTheme: Boolean) {
        settingsInteractor.setDarkTheme(isDarkTheme)
        darkThemeLiveData.postValue(isDarkTheme)
    }

    fun getDarkTheme(): Boolean {
        return settingsInteractor.getDarkTheme()
    }

    fun setAppTheme() {
        settingsInteractor.setAppTheme()
    }


    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun sendSupportEmail() {
        sharingInteractor.sendSupportEmail()
    }

    fun openAgreementUrl() {
        sharingInteractor.openAgreementUrl()
    }
}



