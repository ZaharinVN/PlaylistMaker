package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val track_name: TextView = itemView.findViewById(R.id.track_name)
    private val artist_name: TextView = itemView.findViewById(R.id.artist_name)
    private val track_time: TextView = itemView.findViewById(R.id.track_time)
    private val track_artwork: ImageView = itemView.findViewById(R.id.track_artwork)

    fun bind(track: Track) {
        track_name.text = track.trackName
        artist_name.text = track.artistName
        track_time.text = track.trackTime
        Glide.with(itemView.context)
            .load(track.artworkUrl100)
            .into(track_artwork)
    }
}
