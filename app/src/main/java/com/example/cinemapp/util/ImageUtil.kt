package com.example.cinemapp.util

import android.content.Context
import android.content.res.Resources.Theme
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.cinemapp.R

fun loadImage(path: String, imageView: ImageView, context: Context, placeholder: Int) {
    Glide.with(context)
        .load(path.ifEmpty {
            ResourcesCompat.getDrawable(
                context.resources,
                placeholder,
                null
            )
        })
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .into(imageView)
}