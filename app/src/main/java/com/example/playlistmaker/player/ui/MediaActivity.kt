package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.provider.MediaStore.Audio.AudioColumns.TRACK
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.R
import com.google.gson.Gson

class MediaActivity : AppCompatActivity() {
    private lateinit var viewModel: MediaViewModel
    private var binding: ActivityMediaBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val track = getTrack()
        bind(track)

        viewModel = ViewModelProvider(
            this, MediaViewModel
                .getViewModelFactory(track.previewUrl)
        )[MediaViewModel::class.java]

        viewModel.observeState().observe(this) {
            updateScreen(it)
        }

        viewModel.observeTimer().observe(this) {
            updateTimer(it)
        }

        binding?.btnPlay?.setOnClickListener {
            if (viewModel.isClickAllowed()) {
                viewModel.playbackControl()
            }
        }

        binding?.btnPlayerBack?.setOnClickListener {
            finish()
        }
    }

    private fun bind(track: TrackPlayerModel) {
        val radius = resources.getDimensionPixelSize(R.dimen.cover_radius).toFloat()

        Glide.with(this)
            .load(track.getCoverArtwork())
            .transform(RoundedCorners(radius.toInt()))
            .placeholder(R.drawable.placeholder)
            .into(binding?.trackCover!!)

        binding?.apply {
            trackNameResult.text = track.trackName
            artistNameResult.text = track.artistName
            trackTimeResult.text = getString(R.string.default_playtime_value)
            progressTime.text = track.formatTrackDuration()
            collectionName.text = track.collectionName
            releaseDate.text = track.formatReleaseDate()
            primaryGenreName.text = track.primaryGenreName
            country.text = track.country

            trackNameResult.isSelected = true
            artistNameResult.isSelected = true
        }
    }

    private fun getTrack() =
        Gson().fromJson(intent.getStringExtra(TRACK), TrackPlayerModel::class.java)

    private fun updateTimer(time: String) {
        binding?.progressTime?.text = time
    }

    private fun updateScreen(state: MediaPlayerState) {
        when (state) {
            is MediaPlayerState.Playing -> {
                binding?.btnPlay?.setImageResource(R.drawable.ic_pause)
            }

            is MediaPlayerState.Paused -> {
                binding?.btnPlay?.setImageResource(R.drawable.ic_play)
            }

            is MediaPlayerState.Prepared -> {
                binding?.btnPlay?.setImageResource(R.drawable.ic_play)
                binding?.progressTime?.setText(R.string.default_playtime_value)
            }
            else -> {}
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }
}






















