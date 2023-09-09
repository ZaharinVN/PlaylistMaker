package com.example.playlistmaker

import android.content.Intent
import android.widget.ImageButton
import android.widget.TextView
import com.example.playlistmaker.data.dto.MediaDataSource
import com.example.playlistmaker.domain.MediaContract
import com.example.playlistmaker.domain.api.MediaRepository
import com.example.playlistmaker.presentation.MediaPresenter


object Creator {
    fun createMediaRepository(intent: Intent): MediaRepository {
        return MediaDataSource(intent)
    }

    fun createMediaPresenter(
        btnPlay: ImageButton,
        btnPause: ImageButton,
        progressTime: TextView,
        btnFavorite: ImageButton,
        btnDisLike: ImageButton,
        previewUrl: String?,
    ): MediaContract.Presenter {
        return MediaPresenter(btnPlay, btnPause, progressTime, btnFavorite, btnDisLike, previewUrl)
    }
}
