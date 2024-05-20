package com.example.cinemapp.ui.main

sealed class Media {

    data class Image(
        val filePath: String = ""
    ) : Media()

    data class Video(
        val html: String = "<html></html>",
        val site: String = "",
        val type: String = "",
        val official: Boolean = false
    ) : Media()
}
