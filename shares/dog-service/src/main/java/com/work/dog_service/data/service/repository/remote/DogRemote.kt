package com.work.dog_service.data.service.repository.remote

import com.work.dog_service.data.model.response.DogResponse
import com.work.dog_service.data.service.api.DogService
import com.work.network.base.ApiManager
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory

interface IDogRemote {
    suspend fun getRandomDog(): Response<DogResponse>
}

class DogRemote(
    private val apiManager: ApiManager
): IDogRemote {

    private fun getDogService(): DogService {
        return apiManager.init(
            baseUrl = "https://dog.ceo", //todo refactor to build config
            converter = GsonConverterFactory.create()
        ).create(DogService::class.java)
    }

    override suspend fun getRandomDog(): Response<DogResponse> {
        return getDogService().getRandomDog()
    }
}