package com.example.playlistmaker.di

import com.example.playlistmaker.library.ui.history.HistoryViewModel
import com.example.playlistmaker.library.ui.viewModel.FavoriteViewModel
import com.example.playlistmaker.library.ui.viewModel.PlaylistViewModel
import com.example.playlistmaker.player.ui.viewModel.PlayerViewModel
import com.example.playlistmaker.search.ui.viewModel.SearchViewModel
import com.example.playlistmaker.settings.ui.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { (trackUrl: String) ->
        PlayerViewModel(
            playerInteractor = get(),
            trackUrl = trackUrl
        )
    }

    viewModel {
        SettingsViewModel(settingsInteractor = get(), sharingInteractor = get())
    }

    viewModel {
        SearchViewModel(app = get(), searchInteractor = get())
    }

    viewModel {
        FavoriteViewModel()
    }

    viewModel {
        PlaylistViewModel()
    }

    viewModel {
        HistoryViewModel(androidContext(), get())
    }
}
