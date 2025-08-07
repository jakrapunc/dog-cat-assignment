package com.work.cat_service.data.service.repository.remote


import com.work.cat_service.data.model.response.CatBreedsResponse
import com.work.cat_service.data.service.api.CatService
import com.work.network.base.ApiManager
import retrofit2.Response

import retrofit2.converter.gson.GsonConverterFactory

interface ICatRemote {
    suspend fun getCatBreeds(
        limit: Int
    ): Response<CatBreedsResponse>
}

class CatRemote(
    private val apiManager: ApiManager,
): ICatRemote {

    private fun getCatService(): CatService {
        return apiManager.init(
            baseUrl = "https:catfact.ninja", //todo refactor to build config
            converter = GsonConverterFactory.create()
        ).create(CatService::class.java)
    }

    override suspend fun getCatBreeds(limit: Int): Response<CatBreedsResponse> {
        return getCatService().getCatBreeds(
            limit = limit
        )
    }
}