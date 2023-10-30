package com.example.playlistmaker.di

import com.example.playlistmaker.library.ui.view_model.FavoriteViewModel
import com.example.playlistmaker.library.ui.view_model.PlaylistViewModel
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.settings.ui.SettingsViewModel
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
}
