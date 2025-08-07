package com.work.cat_service.data.model

import com.google.gson.annotations.SerializedName

data class CatBreedItem(
    @SerializedName("breed")
    val breed: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("origin")
    val origin: String,
    @SerializedName("coat")
    val coat: String,
    @SerializedName("pattern")
    val pattern: String
)