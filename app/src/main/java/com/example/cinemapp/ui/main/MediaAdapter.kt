package com.example.cinemapp.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemapp.R
import com.example.cinemapp.databinding.CardImageBinding
import java.lang.IllegalArgumentException

class MediaAdapter : RecyclerView.Adapter<MediaViewHolder>() {

    private var media: List<Media> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setMedia(newMedia: List<Media>) {
        media = newMedia
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        when (viewType) {
            R.layout.card_image -> {
                val binding = CardImageBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                return MediaViewHolder.ImageViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid Media view type.")
        }
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        when (holder) {
            is MediaViewHolder.ImageViewHolder -> holder.bind(media[position] as Media.Image)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (media[position]) {
            is Media.Image -> R.layout.card_image
        }
    }

    override fun getItemCount(): Int {
        return media.size
    }

}