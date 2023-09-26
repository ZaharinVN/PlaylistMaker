package com.example.playlistmaker.main.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MainViewModel : ViewModel() {
    private val _darkModeEnabled = MutableLiveData<Boolean>()
    val darkModeEnabled: LiveData<Boolean> = _darkModeEnabled

    fun setDarkModeEnabled(enabled: Boolean) {
        _darkModeEnabled.value = enabled
    }
}

