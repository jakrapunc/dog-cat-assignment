package com.work.dog_service.data.service.repository

import com.work.dog_service.data.model.response.DogResponse
import com.work.dog_service.data.service.repository.remote.IDogRemote
import com.work.network.repository.NetworkBoundResource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface IDogRepository {
    suspend fun getRandomDog(): Flow<DogResponse>
}

class DogRepository(
    private val dogRemote: IDogRemote
): IDogRepository {
    override suspend fun getRandomDog(): Flow<DogResponse> {
        return object : NetworkBoundResource<DogResponse>() {
            override suspend fun createCall(): Response<DogResponse> {
                return dogRemote.getRandomDog()
            }
        }.asFlow()
    }
}