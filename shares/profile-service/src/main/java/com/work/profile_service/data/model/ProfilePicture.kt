package com.work.profile_service.data.model

import com.google.gson.annotations.SerializedName

data class ProfilePicture(
    @SerializedName("large")
    val large: String,
    @SerializedName("medium")
    val medium: String,
    @SerializedName("thumbnail")
    val thumbnail: String
)