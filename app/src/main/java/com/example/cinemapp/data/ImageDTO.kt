package com.example.cinemapp.data

import com.google.gson.annotations.SerializedName

data class ImageDTO(
    @SerializedName("file_path") val filePath: String? = null
)
