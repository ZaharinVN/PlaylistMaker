package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.SearchActivity.Companion.EXTRA_ARTIST_NAME
import com.example.playlistmaker.SearchActivity.Companion.EXTRA_COLLECTION_NAME
import com.example.playlistmaker.SearchActivity.Companion.EXTRA_COUNTRY
import com.example.playlistmaker.SearchActivity.Companion.EXTRA_PREVIEW
import com.example.playlistmaker.SearchActivity.Companion.EXTRA_PRIMARY_GENRE_NAME
import com.example.playlistmaker.SearchActivity.Companion.EXTRA_RELEASE_DATE
import com.example.playlistmaker.SearchActivity.Companion.EXTRA_TRACK_COVER
import com.example.playlistmaker.SearchActivity.Companion.EXTRA_TRACK_NAME
import com.example.playlistmaker.SearchActivity.Companion.EXTRA_TRACK_TIME
import com.example.playlistmaker.databinding.ActivityMediaBinding
import android.media.MediaPlayer
import android.media.AudioAttributes
import android.os.Handler
import android.widget.TextView

class MediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaBinding
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying: Boolean = false
    private var currentPosition: Int = 0
    private var progressHandler: Handler = Handler()
    private lateinit var progressRunnable: Runnable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnPlayerBack.setOnClickListener { finish() }

        val trackCoverUrl = intent.getStringExtra(EXTRA_TRACK_COVER)
        val trackName = intent.getStringExtra(EXTRA_TRACK_NAME)
        val artistName = intent.getStringExtra(EXTRA_ARTIST_NAME)
        val trackTime = intent.getLongExtra(EXTRA_TRACK_TIME, 0)
        val collectionName = intent.getStringExtra(EXTRA_COLLECTION_NAME)
        val releaseDate = intent.getStringExtra(EXTRA_RELEASE_DATE)
        val primaryGenreName = intent.getStringExtra(EXTRA_PRIMARY_GENRE_NAME)
        val country = intent.getStringExtra(EXTRA_COUNTRY)
        val previewUrl = intent.getStringExtra(EXTRA_PREVIEW)

        val radius = resources.getDimensionPixelSize(R.dimen.cover_radius).toFloat()
        Glide.with(this)
            .load(trackCoverUrl?.let { ItunesSearchResult.getCoverArtwork(it) })
            .transform(RoundedCorners(radius.toInt()))
            .placeholder(R.drawable.placeholder)
            .into(binding.trackCover)

        binding.trackNameResult.text = trackName
        binding.artistNameResult.text = artistName
        binding.trackTimeResult.text = ItunesSearchResult.formatTrackDuration(trackTime)
        binding.collectionName.text = collectionName
        binding.releaseDate.text = ItunesSearchResult.formatReleaseDate(releaseDate.toString())
        binding.primaryGenreName.text = primaryGenreName
        binding.country.text = country
        binding.progressTime.text = "00:00"

        //btnAdd.setOnClickListener("ADD")
        //btnFavorite.setOnClickListener("Favorite")
        val btnPlay = findViewById<ImageButton>(R.id.btnPlay)
        val btnPause = findViewById<ImageButton>(R.id.btnPause)
        val progressTime = findViewById<TextView>(R.id.progressTime)

        btnPlay.setOnClickListener {
            btnPause.visibility = View.VISIBLE
            if (isPlaying) {
                pauseAudio()
            } else {
                playAudio(previewUrl)
            }
        }
        btnPause.setOnClickListener {
            btnPause.visibility = View.GONE
            pauseAudio()
        }
        progressRunnable = Runnable {
            updateProgressTime(progressTime)
            progressHandler.postDelayed(progressRunnable, 1000)
        }
    }

    private fun playAudio(audioUrl: String?) {
        try {
            if (isPlaying) {
                mediaPlayer?.seekTo(currentPosition)
                mediaPlayer?.start()
            } else {
                mediaPlayer = MediaPlayer().apply {
                    setAudioAttributes(AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build())
                    setDataSource(audioUrl)
                    prepare()
                    start()
                    progressHandler.post(progressRunnable)
                }
            }
            isPlaying = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun pauseAudio() {
        try {
            mediaPlayer?.pause()
            currentPosition = mediaPlayer?.currentPosition ?: 0
            isPlaying = false
            progressHandler.removeCallbacks(progressRunnable)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateProgressTime(progressTime: TextView) {
        val progress = mediaPlayer?.currentPosition ?: 0
        val duration = mediaPlayer?.duration ?: 0
        progressTime.text = "${formatTime(progress)}"
    }

    private fun formatTime(timeInMillis: Int): String {
        val minutes = timeInMillis / 1000 / 60
        val seconds = timeInMillis / 1000 % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        progressHandler.removeCallbacks(progressRunnable)
        mediaPlayer?.release()
    }
}





