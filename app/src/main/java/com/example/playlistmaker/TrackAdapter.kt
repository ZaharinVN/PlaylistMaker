package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(private val data: List<ItunesSearchResult>
) : RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_track, parent, false)
        return TrackViewHolder(view)
    }
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size
}