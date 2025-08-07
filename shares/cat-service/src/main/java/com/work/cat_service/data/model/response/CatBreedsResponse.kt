package com.work.cat_service.data.model.response

import com.google.gson.annotations.SerializedName
import com.work.cat_service.data.model.CatBreedItem

data class CatBreedsResponse(
    @SerializedName("current_page")
    val currentPage: Int = 1,
    @SerializedName("data")
    val data: List<CatBreedItem> = emptyList(),
    @SerializedName("last_page")
    val lastPage: Int = 0,
)