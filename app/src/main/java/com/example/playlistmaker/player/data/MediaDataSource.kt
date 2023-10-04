package com.example.playlistmaker.player.data

import android.content.Intent
import com.example.playlistmaker.player.domain.api.MediaRepository


class MediaDataSource(private val intent: Intent) : MediaRepository {
    override fun getTrackCoverUrl(): String? {
        return intent.getStringExtra(EXTRA_TRACK_COVER)
    }

    override fun getTrackName(): String {
        return intent.getStringExtra(EXTRA_TRACK_NAME).toString()
    }

    override fun getArtistName(): String {
        return intent.getStringExtra(EXTRA_ARTIST_NAME).toString()
    }

    override fun getTrackTime(): String {
        return intent.getLongExtra(EXTRA_TRACK_TIME, 0).toString()
    }

    override fun getCollectionName(): String {
        return intent.getStringExtra(EXTRA_COLLECTION_NAME).toString()
    }

    override fun getReleaseDate(): String {
        return intent.getStringExtra(EXTRA_RELEASE_DATE).toString()
    }

    override fun getPrimaryGenreName(): String {
        return intent.getStringExtra(EXTRA_PRIMARY_GENRE_NAME).toString()
    }

    override fun getCountry(): String {
        return intent.getStringExtra(EXTRA_COUNTRY).toString()
    }

    override fun getPreviewUrl(): String {
        return intent.getStringExtra(EXTRA_PREVIEW).toString()
    }

    companion object {
        const val EXTRA_TRACK_NAME = "trackName"
        const val EXTRA_ARTIST_NAME = "artistName"
        const val EXTRA_TRACK_TIME = "trackTimeMillis"
        const val EXTRA_TRACK_COVER = "trackCover"
        const val EXTRA_COLLECTION_NAME = "collectionName"
        const val EXTRA_RELEASE_DATE = "releaseDate"
        const val EXTRA_PRIMARY_GENRE_NAME = "primaryGenreName"
        const val EXTRA_COUNTRY = "country"
        const val EXTRA_PREVIEW = "previewUrl"
    }
}