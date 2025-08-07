package com.work.cat_service.data.service.api

import com.work.cat_service.data.model.CatBreedItem
import com.work.cat_service.data.model.response.CatBreedsResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CatService {
    @GET("/breeds")
    suspend fun getCatBreeds(
        @Query("limit") limit: Int,
        @Query("page") page: Int? = null
    ): Response<CatBreedsResponse>

}