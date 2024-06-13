package com.example.cinemapp.ui.main.movie_details

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.cinemapp.R
import com.example.cinemapp.databinding.CardEmbeddedVideoBinding
import com.example.cinemapp.databinding.CardImageBinding
import com.example.cinemapp.ui.main.model.Media
import com.example.cinemapp.util.loadImage

sealed class MediaViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class ImageViewHolder(private val binding: CardImageBinding) : MediaViewHolder(binding) {
        fun bind(image: Media.Image) {
            with(binding) {
                loadImage(image.filePath, ivImage, root.context, R.drawable.ic_placeholder_movie)
            }
        }
    }

    class VideoViewHolder(private val binding: CardEmbeddedVideoBinding) : MediaViewHolder(binding) {
        @SuppressLint("SetJavaScriptEnabled")
        fun bind(video: Media.Video) {
            with(binding) {
                wvVideo.loadData(video.html, "text/html", "utf-8")
                wvVideo.settings.javaScriptEnabled = true
                wvVideo.webChromeClient = WebChromeClient()
            }
        }
    }

}