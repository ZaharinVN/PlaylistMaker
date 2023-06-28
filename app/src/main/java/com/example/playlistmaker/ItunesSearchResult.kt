package com.example.playlistmaker

import android.icu.util.TimeUnit
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ItunesSearchResult(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
     val artworkUrl100: String
     )
