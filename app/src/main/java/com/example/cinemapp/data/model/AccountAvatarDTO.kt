package com.example.cinemapp.data.model

import com.google.gson.annotations.SerializedName

data class AccountAvatarDTO(
    @SerializedName("gravatar") val gravatar: GravatarAvatarDTO?,
    @SerializedName("tmdb") val tmdb: TmdbAvatarDTO?
)

data class GravatarAvatarDTO(
    @SerializedName("hash") val hash: String?
)

data class TmdbAvatarDTO(
    @SerializedName("avatar_path") val avatarPath: String?
)
