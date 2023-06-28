package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)
    private val trackArtwork: ImageView = itemView.findViewById(R.id.track_artwork)

    fun bind(track: ItunesSearchResult) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        val duration = (track.trackTimeMillis.toLong() / 1000).toInt()
        trackTime.text = String.format("%02d:%02d", duration / 60, duration % 60)
        Glide.with(itemView.context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .apply(RequestOptions.bitmapTransform(RoundedCorners(5)))
            .into(trackArtwork)
    }
    }
