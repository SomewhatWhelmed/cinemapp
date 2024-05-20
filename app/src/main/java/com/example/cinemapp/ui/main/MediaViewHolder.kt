package com.example.cinemapp.ui.main

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.cinemapp.databinding.CardEmbeddedVideoBinding
import com.example.cinemapp.databinding.CardImageBinding

sealed class MediaViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class ImageViewHolder(private val binding: CardImageBinding) : MediaViewHolder(binding) {
        fun bind(image: Media.Image) {
            with(binding) {
                Glide.with(root.context).load(image.filePath).into(binding.ivImage)
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