package com.example.cinemapp.ui.main.movie_details

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemapp.R
import com.example.cinemapp.databinding.CardEmbeddedVideoBinding
import com.example.cinemapp.databinding.CardImageBinding
import com.example.cinemapp.ui.main.model.Media
import java.lang.IllegalArgumentException

class MediaAdapter : RecyclerView.Adapter<MediaViewHolder>() {

    private var media: List<Media> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setMedia(newMedia: List<Media>) {
        media = newMedia
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        return when (viewType) {
            R.layout.card_image -> {
                val binding = CardImageBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                MediaViewHolder.ImageViewHolder(binding)
            }

            R.layout.card_embedded_video -> {
                val binding = CardEmbeddedVideoBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                MediaViewHolder.VideoViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid Media view type.")
        }
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        when (holder) {
            is MediaViewHolder.ImageViewHolder -> holder.bind(media[position] as Media.Image)
            is MediaViewHolder.VideoViewHolder -> holder.bind(media[position] as Media.Video)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (media[position]) {
            is Media.Image -> R.layout.card_image
            is Media.Video -> R.layout.card_embedded_video
        }
    }

    override fun getItemCount(): Int {
        return media.size
    }

}