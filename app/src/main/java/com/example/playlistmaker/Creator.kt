package com.example.playlistmaker

import android.content.Intent
import com.example.playlistmaker.data.dto.MediaDataSource
import com.example.playlistmaker.domain.api.MediaRepository

object Creator {
    fun createMediaRepository(intent: Intent): MediaRepository {
        return MediaDataSource(intent)
    }
}
