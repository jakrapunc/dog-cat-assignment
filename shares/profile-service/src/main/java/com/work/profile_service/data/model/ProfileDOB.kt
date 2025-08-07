package com.work.profile_service.data.model

import com.google.gson.annotations.SerializedName

data class ProfileDOB(
    @SerializedName("date")
    val date: String,
    @SerializedName("age")
    val age: Int
)