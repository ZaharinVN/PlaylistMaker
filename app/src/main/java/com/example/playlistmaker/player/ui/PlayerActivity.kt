package com.example.playlistmaker.player.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.TrackPlayerModel
import com.example.playlistmaker.player.domain.api.PlayerState
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaBinding
    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(getTrack()!!.previewUrl)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val track = getTrack()
        bind(track)

        if (track != null) {
            viewModel.observeState().observe(this) {
                updateScreen(it)
            }
            viewModel.observeTimer().observe(this) {
                updateTimer(it)
            }
        }

        binding.btnPlay.setOnClickListener {
            viewModel.onPlay()
        }
        binding.btnFavorite.setOnClickListener {
            binding.btnDisLike.visibility = View.VISIBLE
        }
        binding.btnDisLike.setOnClickListener {
            binding.btnDisLike.visibility = View.GONE
        }

        binding.btnPlayerBack.setOnClickListener {
            finish()
        }
    }

    private fun getTrack(): TrackPlayerModel? {
        return intent.getSerializableExtra(EXTRA_TRACK) as? TrackPlayerModel
    }

    private fun bind(track: TrackPlayerModel?) {
        track?.let {
            val radius = resources.getDimensionPixelSize(R.dimen.cover_radius)
            binding?.let {
                Glide.with(this)
                    .load(track.getCoverArtwork())
                    .placeholder(R.drawable.placeholder)
                    .transform(RoundedCorners(radius))
                    .into(it.trackCover)
            }
            binding?.apply {
                trackNameResult.text = it.trackName
                artistNameResult.text = it.artistName
                trackTimeResult.text = it.formatTrackDuration()
                progressTime.text = it.formatTrackDuration()
                collectionName.text = it.collectionName
                releaseDate.text = it.formatReleaseDate()
                primaryGenreName.text = it.primaryGenreName
                country.text = it.country
            }
        }
    }

    private fun updateTimer(time: String) {
        binding.progressTime.text = time
    }

    private fun updateScreen(state: PlayerState) {

        when (state) {
            is PlayerState.Playing -> {
                binding.btnPlay.setImageResource(
                    if (isDarkTheme()) R.drawable.ic_pause_dark else R.drawable.ic_pause
                )
            }

            is PlayerState.Paused -> {
                binding.btnPlay.setImageResource(
                    if (isDarkTheme()) R.drawable.ic_play_dark else R.drawable.ic_play
                )
            }

            is PlayerState.Prepared -> {
                binding.btnPlay.setImageResource(
                    if (isDarkTheme()) R.drawable.ic_play_dark else R.drawable.ic_play
                )
                binding.progressTime.setText(R.string.default_playtime_value)
            }

            else -> {}
        }
    }

    private fun isDarkTheme(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    companion object {
        private const val EXTRA_TRACK = "EXTRA_TRACK"
    }
}
























