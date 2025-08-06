package com.work.profile_service.data.model

import com.google.gson.annotations.SerializedName

data class ProfileLocation(
    @SerializedName("city")
    val city: String,
    @SerializedName("street")
    val street: LocationStreet,
    @SerializedName("state")
    val state: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("postcode")
    val postcode: String
) {
    data class LocationStreet(
        @SerializedName("number")
        val number: Int,
        @SerializedName("name")
        val name: String
    )
}