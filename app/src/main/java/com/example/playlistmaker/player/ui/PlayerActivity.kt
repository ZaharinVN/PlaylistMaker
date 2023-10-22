package com.example.playlistmaker.player.ui


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.TrackPlayerModel
import com.example.playlistmaker.player.domain.api.PlayerState
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.roundToInt

class PlayerActivity : AppCompatActivity() {
    private var binding: ActivityMediaBinding? = null
    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(getTrack()!!.previewUrl)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        bind(getTrack())

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
        binding?.let {
            Glide.with(this)
                .load(track.getCoverArtwork())
                .placeholder(R.drawable.placeholder)
                .transform(RoundedCorners(radius.roundToInt()))
                .into(it.trackCover)
        }
        binding?.apply {
            trackNameResult.text = track.trackName
            artistNameResult.text = track.artistName
            trackTimeResult.text = track.formatTrackDuration()
            progressTime.text = track.formatTrackDuration()
            collectionName.text = track.collectionName
            releaseDate.text = track.formatReleaseDate()
            primaryGenreName.text = track.primaryGenreName
            country.text = track.country
        }
    }

    private fun getTrack() =
        Gson().fromJson(intent.getStringExtra(EXTRA_TRACK), TrackPlayerModel::class.java)

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

    companion object {
        private const val EXTRA_TRACK = "EXTRA_TRACK"
    }
}
























