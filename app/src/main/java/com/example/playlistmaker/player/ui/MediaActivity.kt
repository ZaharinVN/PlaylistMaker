package com.example.playlistmaker.player.ui


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.search.SearchActivity.Companion.EXTRA_ARTIST_NAME
import com.example.playlistmaker.search.SearchActivity.Companion.EXTRA_COLLECTION_NAME
import com.example.playlistmaker.search.SearchActivity.Companion.EXTRA_COUNTRY
import com.example.playlistmaker.search.SearchActivity.Companion.EXTRA_PREVIEW
import com.example.playlistmaker.search.SearchActivity.Companion.EXTRA_PRIMARY_GENRE_NAME
import com.example.playlistmaker.search.SearchActivity.Companion.EXTRA_RELEASE_DATE
import com.example.playlistmaker.search.SearchActivity.Companion.EXTRA_TRACK_COVER
import com.example.playlistmaker.search.SearchActivity.Companion.EXTRA_TRACK_NAME
import com.example.playlistmaker.search.SearchActivity.Companion.EXTRA_TRACK_TIME
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.ItunesSearchResult
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.api.MediaRepository

class MediaActivity : AppCompatActivity(), MediaContract {
    private lateinit var presenter: MediaContract.Presenter
    private lateinit var binding: ActivityMediaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = getIntent()
        val repository: MediaRepository = Creator.createMediaRepository(intent)

        presenter = MediaPresenter(
            binding.btnPlay,
            binding.btnPause,
            binding.progressTime,
            binding.btnFavorite,
            binding.btnDisLike,
            intent.getStringExtra(EXTRA_PREVIEW),
            Creator.createInteractor(intent.getStringExtra(EXTRA_PREVIEW))
        )

        // Настройка обработчиков событий
        binding.btnPlay.setOnClickListener { presenter.onPlayClicked() }
        binding.btnPause.setOnClickListener { presenter.onPauseAudioClicked() }
        binding.btnFavorite.setOnClickListener { presenter.onFavoriteClicked() }
        binding.btnDisLike.setOnClickListener { presenter.onDisLikeClicked() }
        binding.btnPlayerBack.setOnClickListener { finish() }

        val trackCoverUrl = intent.getStringExtra(EXTRA_TRACK_COVER)
        val trackName = intent.getStringExtra(EXTRA_TRACK_NAME)
        val artistName = intent.getStringExtra(EXTRA_ARTIST_NAME)
        val trackTime = intent.getLongExtra(EXTRA_TRACK_TIME, 0)
        val collectionName = intent.getStringExtra(EXTRA_COLLECTION_NAME)
        val releaseDate = intent.getStringExtra(EXTRA_RELEASE_DATE)
        val primaryGenreName = intent.getStringExtra(EXTRA_PRIMARY_GENRE_NAME)
        val country = intent.getStringExtra(EXTRA_COUNTRY)

        binding.trackNameResult.text = trackName
        binding.artistNameResult.text = artistName
        binding.trackTimeResult.text = ItunesSearchResult.formatTrackDuration(trackTime)
        binding.collectionName.text = collectionName
        binding.releaseDate.text = ItunesSearchResult.formatReleaseDate(releaseDate.toString())
        binding.primaryGenreName.text = primaryGenreName
        binding.country.text = country
        binding.progressTime.text = "00:00"

        val radius = resources.getDimensionPixelSize(R.dimen.cover_radius).toFloat()
        Glide.with(this)
            .load(trackCoverUrl?.let { ItunesSearchResult.getCoverArtwork(it) })
            .transform(RoundedCorners(radius.toInt()))
            .placeholder(R.drawable.placeholder)
            .into(binding.trackCover)
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }
}








