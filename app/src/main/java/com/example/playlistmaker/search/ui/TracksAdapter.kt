package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.databinding.LayoutTrackBinding
import com.example.playlistmaker.search.domain.model.TrackSearchModel


class TracksAdapter(
    private val tracks: ArrayList<TrackSearchModel>,
    private val clickListener: TrackClickListener
) : RecyclerView.Adapter<TracksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        return TracksViewHolder(
            LayoutTrackBinding
                .inflate(
                    LayoutInflater
                        .from(parent.context), parent, false
                )
        )
    }

    override fun getItemCount() = tracks.size

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { clickListener.onTrackClick(tracks.get(position)) }
        animateItemClick(holder.itemView)
    }

    fun interface TrackClickListener {
        fun onTrackClick(track: TrackSearchModel)
    }
    private fun animateItemClick(view: View) {
        val anim = AnimationUtils.loadAnimation(view.context, R.anim.click_animation)
        view.startAnimation(anim)
    }
}