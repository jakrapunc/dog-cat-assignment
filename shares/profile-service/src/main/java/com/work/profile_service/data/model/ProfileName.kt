package com.work.profile_service.data.model

import com.google.gson.annotations.SerializedName

data class ProfileName(
    @SerializedName("title")
    val title: String,
    @SerializedName("first")
    val first: String,
    @SerializedName("last")
    val last: String
)