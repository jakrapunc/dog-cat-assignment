package com.work.profile_service.data.model.response

import com.google.gson.annotations.SerializedName
import com.work.profile_service.data.model.ProfileData

data class ProfileResponse(
    @SerializedName("results")
    val results: List<ProfileData>
)