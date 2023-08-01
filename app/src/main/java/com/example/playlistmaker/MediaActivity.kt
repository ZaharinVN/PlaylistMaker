package com.example.playlistmaker


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.SearchActivity.Companion.EXTRA_ARTIST_NAME
import com.example.playlistmaker.SearchActivity.Companion.EXTRA_COLLECTION_NAME
import com.example.playlistmaker.SearchActivity.Companion.EXTRA_COUNTRY
import com.example.playlistmaker.SearchActivity.Companion.EXTRA_PRIMARY_GENRE_NAME
import com.example.playlistmaker.SearchActivity.Companion.EXTRA_RELEASE_DATE
import com.example.playlistmaker.SearchActivity.Companion.EXTRA_TRACK_COVER
import com.example.playlistmaker.SearchActivity.Companion.EXTRA_TRACK_NAME
import com.example.playlistmaker.SearchActivity.Companion.EXTRA_TRACK_TIME

class MediaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        // Получение информации из Extra и установка значений в соответствующие View
        val trackCoverUrl = intent.getStringExtra(EXTRA_TRACK_COVER)
        val trackName = intent.getStringExtra(EXTRA_TRACK_NAME)
        val artistName = intent.getStringExtra(EXTRA_ARTIST_NAME)
        val trackTime = intent.getLongExtra(EXTRA_TRACK_TIME, 0)
        val btnAdd = findViewById<ImageView>(R.id.btnAdd)
        val btnPlay = findViewById<ImageView>(R.id.btnPlay)
        val btnFavorite = findViewById<ImageView>(R.id.btnFavorite)
        val collectionName = intent.getStringExtra(EXTRA_COLLECTION_NAME)
        val releaseDate = intent.getStringExtra(EXTRA_RELEASE_DATE)
        val primaryGenreName = intent.getStringExtra(EXTRA_PRIMARY_GENRE_NAME)
        val country = intent.getStringExtra(EXTRA_COUNTRY)
        val iv_trackCover = findViewById<ImageView>(R.id.track_cover)
        val radius = resources.getDimensionPixelSize(R.dimen.cover_radius)
            .toFloat()
        Glide.with(this)
            .load(trackCoverUrl?.let { getCoverArtwork(it) })
            .transform(RoundedCorners(radius.toInt()))
            .placeholder(R.drawable.placeholder)
            .into(iv_trackCover)
        val tv_trackName = findViewById<TextView>(R.id.trackNameResult)
        tv_trackName.text = trackName

        val tv_artistName = findViewById<TextView>(R.id.artistNameResult)
        tv_artistName.text = artistName

        val tv_trackDuration = findViewById<TextView>(R.id.trackTimeResult)
        tv_trackDuration.text = formatTrackDuration(trackTime)

        val tv_collectionName = findViewById<TextView>(R.id.collection_Name)
        tv_collectionName.text = collectionName

        val tv_releaseDate = findViewById<TextView>(R.id.release_Date)
        tv_releaseDate.text = formatReleaseDate(releaseDate.toString())

        val tv_genreName = findViewById<TextView>(R.id.primary_GenreName)
        tv_genreName.text = primaryGenreName

        val tv_country = findViewById<TextView>(R.id.country)
        tv_country.text = country

        val backButton = findViewById<Button>(R.id.btnPlayerBack)
        backButton.setOnClickListener { finish() }

        //btnAdd.setOnClickListener("ADD")
        //btnPlay.setOnClickListener("Play")
        //btnFavorite.setOnClickListener("Favorite")
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




