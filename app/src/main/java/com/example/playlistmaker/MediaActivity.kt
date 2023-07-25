package com.example.playlistmaker


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.playlistmaker.SearchActivity.Companion.EXTRA_TRACK_COVER

class MediaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        // Получение информации из Extra и установка значений в соответствующие View
        val trackCoverUrl = intent.getStringExtra(EXTRA_TRACK_COVER)
        val trackName = intent.getStringExtra("trackName")
        val artistName = intent.getStringExtra("artistName")
        val trackTime = intent.getLongExtra("trackTimeMillis", 0)
        val collectionName = intent.getStringExtra("collectionName")
        val releaseDate = intent.getStringExtra("releaseDate")
        val primaryGenreName = intent.getStringExtra("primaryGenreName")
        val country = intent.getStringExtra("country")

        val trackCoverImageView = findViewById<ImageView>(R.id.track_cover)
        Glide.with(this)
            .load(trackCoverUrl?.let { getCoverArtwork(it) })
            .into(trackCoverImageView)

        val trackNameTextView = findViewById<TextView>(R.id.trackNameResult)
        trackNameTextView.text = trackName

        val artistNameTextView = findViewById<TextView>(R.id.artistNameResult)
        artistNameTextView.text = artistName

        val trackDurationTextView = findViewById<TextView>(R.id.trackTimeResult)
        trackDurationTextView.text = formatTrackDuration(trackTime)

        val collectionNameTextView = findViewById<TextView>(R.id.collection_Name)
        collectionNameTextView.text = collectionName

        val releaseDateTextView = findViewById<TextView>(R.id.release_Date)
        releaseDateTextView.text = formatReleaseDate(releaseDate.toString())

        val genreNameTextView = findViewById<TextView>(R.id.primary_GenreName)
        genreNameTextView.text = primaryGenreName

        val countryTextView = findViewById<TextView>(R.id.country)
        countryTextView.text = country

        val backButton = findViewById<Button>(R.id.btnPlayerBack)
        backButton.setOnClickListener { finish() }
    }

    private fun getCoverArtwork(artworkUrl100: String): String {
        return artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    }

    private fun formatTrackDuration(duration: Long): String {
        val minutes = duration / 60000
        val seconds = (duration % 60000) / 1000
        return String.format("%02d:%02d", minutes, seconds)
    }


    private fun formatReleaseDate(date: String): String {
        return date.substring(0, 4)
    }
}




