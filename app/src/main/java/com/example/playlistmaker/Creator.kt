package com.example.playlistmaker

import android.content.Intent
import android.widget.ImageButton
import android.widget.TextView
import com.example.playlistmaker.data.PlayerImpl
import com.example.playlistmaker.data.dto.MediaDataSource
import com.example.playlistmaker.presentation.ui.MediaContract
import com.example.playlistmaker.domain.Player
import com.example.playlistmaker.domain.PlayerInteractor
import com.example.playlistmaker.domain.api.MediaRepository
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.presentation.MediaPresenter


object Creator {
    fun createMediaRepository(intent: Intent): MediaRepository {
        return MediaDataSource(intent)
    }

}
