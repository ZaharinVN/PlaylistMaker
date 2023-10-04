package com.example.playlistmaker.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.R


class MediaActivity : AppCompatActivity() {
    private lateinit var viewModel: MediaViewModel
    private lateinit var binding: ActivityMediaBinding

    companion object {
        const val EXTRA_PREVIEW = "previewUrl"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val repository = Creator.createMediaRepository(intent)

        viewModel = ViewModelProvider(
            this,
            MediaViewModelFactory(repository)
        ).get(MediaViewModel::class.java)

        setupViews()
        observeViewModel()

        viewModel.initialize(
            binding.btnPlay,
            binding.btnPause,
            binding.progressTime,
            binding.btnFavorite,
            binding.btnDisLike,
            intent.getStringExtra(EXTRA_PREVIEW),
            Creator.createInteractor(intent.getStringExtra(EXTRA_PREVIEW))
        )
    }

    private fun setupViews() {
        binding.btnPlay.setOnClickListener { viewModel.onPlayClicked() }
        binding.btnPause.setOnClickListener { viewModel.onPauseAudioClicked() }
        binding.btnFavorite.setOnClickListener { viewModel.onFavoriteClicked() }
        binding.btnDisLike.setOnClickListener { viewModel.onDisLikeClicked() }
        binding.btnPlayerBack.setOnClickListener { finish() }
    }

    private fun observeViewModel() {
        viewModel.trackCoverUrl.observe(this) { trackCoverUrl ->
            val radius = resources.getDimensionPixelSize(R.dimen.cover_radius).toFloat()
            Glide.with(this)
                .load(trackCoverUrl?.let { MediaViewModel.getCoverArtwork(it) })
                .transform(RoundedCorners(radius.toInt()))
                .placeholder(R.drawable.placeholder)
                .into(binding.trackCover)
        }
        viewModel.trackName.observe(this) { trackName ->
            binding.trackNameResult.text = trackName
        }
        viewModel.artistName.observe(this) { artistName ->
            binding.artistNameResult.text = artistName
        }
        viewModel.trackTime.observe(this) { trackTime ->
            binding.trackTimeResult.text =
                MediaViewModel.formatTrackDuration(trackTime.toLong())
        }
        viewModel.collectionName.observe(this) { collectionName ->
            binding.collectionName.text = collectionName
        }
        viewModel.releaseDate.observe(this) { releaseDate ->
            binding.releaseDate.text =
                MediaViewModel.formatReleaseDate(releaseDate.toString())
        }
        viewModel.primaryGenreName.observe(this) { primaryGenreName ->
            binding.primaryGenreName.text = primaryGenreName
        }
        viewModel.country.observe(this) { country ->
            binding.country.text = country
        }
        viewModel.progressTime.observe(this) { progressTime ->
            binding.progressTime.text = progressTime
        }
    }


    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }
}





















