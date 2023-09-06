package com.example.playlistmaker

import android.content.Intent
import com.example.playlistmaker.data.MediaDataSource
import com.example.playlistmaker.data.MediaRepository

object Creator {
    fun createMediaRepository(intent: Intent): MediaRepository {
        return MediaDataSource(intent)
    }
}
