package com.example.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.api.SettingsUseCase
import com.example.playlistmaker.sharing.domain.api.SharingUseCase

class SettingsViewModel(
    private val settingsUseCase: SettingsUseCase,
    private val sharingUseCase: SharingUseCase
) : ViewModel() {
    private val darkThemeLiveData: MutableLiveData<Boolean> = MutableLiveData()

    // функция для получения значения darkThemeLiveData
    fun getDarkThemeLiveData(): LiveData<Boolean> {
        return darkThemeLiveData
    }

    // функция для установки значения darkThemeLiveData
    fun setDarkTheme(isDarkTheme: Boolean) {
        settingsUseCase.setDarkTheme(isDarkTheme)
        darkThemeLiveData.postValue(isDarkTheme)
    }

    // функция для проверки текущего значения darkThemeLiveData
    fun getDarkTheme(): Boolean {
        return settingsUseCase.getDarkTheme()
    }

    // функции для вызова методов sharingUseCase
    fun shareApp() {
        sharingUseCase.shareApp()
    }

    fun sendSupportEmail() {
        sharingUseCase.sendSupportEmail()
    }

    fun openAgreementUrl() {
        sharingUseCase.openAgreementUrl()
    }
}
