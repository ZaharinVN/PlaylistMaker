package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.provider.MediaStore.Audio.AudioColumns.TRACK
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.TrackPlayerModel
import com.example.playlistmaker.player.domain.api.PlayerState
import com.example.playlistmaker.search.domain.model.TrackSearchModel
import com.google.gson.Gson

class PlayerActivity : AppCompatActivity() {
    private var binding: ActivityMediaBinding? = null
    private lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val track = getTrack()
        bind(track)

        if (track != null) {
            viewModel = ViewModelProvider(
                this, PlayerViewModel.getViewModelFactory(track.previewUrl)
            )[PlayerViewModel::class.java]

            viewModel.observeState().observe(this) {
                updateScreen(it)
            }

            viewModel.observeTimer().observe(this) {
                updateTimer(it)
            }
        }

        binding?.btnPlay?.setOnClickListener {
            if (::viewModel.isInitialized && viewModel.isClickAllowed()) {
                viewModel.playbackControl()
            }
        }

        binding?.btnPlayerBack?.setOnClickListener {
            finish()
        }
    }

    private fun bind(track: TrackSearchModel?) {
        track?.let {
            val cornerRadius = this.resources.getDimensionPixelSize(R.dimen.corner_radius)
            binding?.let {
                Glide.with(this)
                    .load(track.getCoverArtwork())
                    .placeholder(R.drawable.placeholder)
                    .transform(RoundedCorners(cornerRadius))
                    .into(it.trackCover)
            }
            binding?.apply {
                trackNameResult.text = it.trackName
                artistNameResult.text = it.artistName
                trackTimeResult.text = getString(R.string.default_playtime_value)
                progressTime.text = it.formatTrackDuration()
                collectionName.text = it.collectionName
                releaseDate.text = it.formatReleaseDate()
                primaryGenreName.text = it.primaryGenreName
                country.text = it.country

            }
        }
    }


    private fun getTrack() =
        Gson().fromJson(intent.getStringExtra(TRACK), TrackSearchModel::class.java)

    private fun updateTimer(time: String) {
        binding?.progressTime?.text = time
    }

    private fun updateScreen(state: PlayerState) {
        when (state) {
            is PlayerState.Playing -> {
                binding?.btnPlay?.setImageResource(R.drawable.ic_pause)
            }

            is PlayerState.Paused -> {
                binding?.btnPlay?.setImageResource(R.drawable.ic_play)
            }

            is PlayerState.Prepared -> {
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






















