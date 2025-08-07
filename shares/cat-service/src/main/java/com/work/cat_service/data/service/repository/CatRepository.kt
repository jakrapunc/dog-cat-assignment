package com.work.cat_service.data.service.repository

import com.work.cat_service.data.model.CatBreedItem
import com.work.cat_service.data.model.request.BreedsRequest
import com.work.cat_service.data.model.response.CatBreedsResponse
import com.work.cat_service.data.service.repository.remote.ICatRemote
import com.work.network.repository.NetworkBoundResource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ICatRepository {
    fun getCatBreeds(
        request: BreedsRequest
    ): Flow<CatBreedsResponse>
}
class CatRepository(
    private val catRemote: ICatRemote
): ICatRepository {

    override fun getCatBreeds(
        request: BreedsRequest
    ): Flow<CatBreedsResponse> {
        return object : NetworkBoundResource<CatBreedsResponse>() {
            override suspend fun createCall(): Response<CatBreedsResponse> {
                return catRemote.getCatBreeds(
                    limit = request.limit
                )
            }
        }.asFlow()
    }
}