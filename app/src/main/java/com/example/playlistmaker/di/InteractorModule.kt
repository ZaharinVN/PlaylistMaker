package com.example.playlistmaker.di

import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<PlayerInteractor> {
        PlayerInteractorImpl(playerRepository = get())
    }

    factory<SettingsInteractor> {
        SettingsInteractorImpl(settingsRepository = get(), sharingRepository = get())
    }

    factory<SearchInteractor> {
        SearchInteractorImpl(repository = get())
    }
}
