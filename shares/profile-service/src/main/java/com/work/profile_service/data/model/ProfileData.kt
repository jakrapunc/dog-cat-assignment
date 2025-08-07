package com.work.profile_service.data.model

import com.google.gson.annotations.SerializedName

data class ProfileData(
    @SerializedName("gender")
    val gender: String,
    @SerializedName("name")
    val name: ProfileName,
    @SerializedName("location")
    val location: ProfileLocation,
    @SerializedName("email")
    val email: String,
    @SerializedName("picture")
    val picture: ProfilePicture,
    @SerializedName("cell")
    val cell: String,
    @SerializedName("dob")
    val dob: ProfileDOB,
    @SerializedName("nat")
    val nat: String
)