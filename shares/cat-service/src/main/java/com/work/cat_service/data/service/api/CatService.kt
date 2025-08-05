package com.work.cat_service.data.service.api

import com.work.cat_service.data.model.CatBreedItem
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface CatService {
    @GET("/breeds")
    fun getCatBreeds(
        @Query("limit") limit: Int
    ): Flow<List<CatBreedItem>>

}