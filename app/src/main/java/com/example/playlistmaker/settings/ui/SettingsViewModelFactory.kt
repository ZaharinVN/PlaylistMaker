package com.example.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.settings.domain.api.SettingsUseCase
import com.example.playlistmaker.sharing.domain.api.SharingUseCase

class SettingsViewModelFactory(
    private val settingsUseCase: SettingsUseCase,
    private val sharingUseCase: SharingUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(settingsUseCase, sharingUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
