package com.work.dog_service.data.service.api

import com.work.dog_service.data.model.response.DogResponse
import retrofit2.Response
import retrofit2.http.GET

interface DogService {
    @GET("/api/breeds/image/random")
    suspend fun getRandomDog(): Response<DogResponse>
}