package com.work.cat_service.data.service.repository

import com.work.cat_service.data.model.CatBreedItem
import com.work.cat_service.data.model.request.BreedsRequest
import com.work.cat_service.data.service.repository.remote.ICatRemote
import com.work.network.repository.NetworkBoundResource
import kotlinx.coroutines.flow.Flow

interface ICatRepository {
    fun getCatBreeds(
        request: BreedsRequest
    ): Flow<List<CatBreedItem>>
}
class CatRepository(
    private val catRemote: ICatRemote
): ICatRepository {

    override fun getCatBreeds(
        request: BreedsRequest
    ): Flow<List<CatBreedItem>> {
        return object : NetworkBoundResource<List<CatBreedItem>>() {
            override suspend fun createCall(): Flow<List<CatBreedItem>> {
                return catRemote.getCatBreeds(
                    limit = request.limit
                )
            }
        }.asFlow()
    }
}