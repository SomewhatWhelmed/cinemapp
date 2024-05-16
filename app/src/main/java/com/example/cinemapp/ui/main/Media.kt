package com.example.cinemapp.ui.main

sealed class Media {

    data class Image(
        val filePath: String = ""
    ) : Media()
}
