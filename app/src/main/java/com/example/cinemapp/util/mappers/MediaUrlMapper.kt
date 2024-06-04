package com.example.cinemapp.util.mappers

import com.example.cinemapp.BuildConfig

class MediaUrlMapper {
    fun mapImageIdToBaseURL(
        imagePath: String?,
        resolution: Int? = null
    ): String {
        return imagePath?.let {
            "${BuildConfig.URL_BASE_IMAGE}${
                (resolution?.let { "w${resolution}" } ?: "original")
            }$imagePath/"
        } ?: ""
    }

    private fun mapYoutubeVideoIdToURL(videoId: String?): String {
        return  "https://www.youtube.com/embed/${videoId ?: ""}?si=lGrXTtU-7EZF6VLE"
    }

    fun mapYoutubeURLToEmbedded(videoId: String?): String {
        return "<iframe " +
                "width=\"100%\" height=\"100%\" " +
                "style=\"top:0; left: 0; position: absolute;\"" +
                "src=\"${mapYoutubeVideoIdToURL(videoId)}\"" +
                "title=\"YouTube video player\" frameborder =\"0\" " +
                "allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" " +
                "referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen>" +
                "</iframe>"
    }
}