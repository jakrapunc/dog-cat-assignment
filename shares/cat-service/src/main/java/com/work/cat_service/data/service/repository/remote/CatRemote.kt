package com.work.cat_service.data.service.repository.remote

import com.work.cat_service.data.model.CatBreedItem
import com.work.cat_service.data.service.api.CatService
import com.work.network.base.ApiManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.converter.gson.GsonConverterFactory

interface ICatRemote {
    fun getCatBreeds(
        limit: Int
    ): Flow<List<CatBreedItem>>
}

class CatRemote(
    private val apiManager: ApiManager,
    private val coroutineDispatcher: CoroutineDispatcher
): ICatRemote {

    private fun getCatService(): CatService {
        return apiManager.init(
            baseUrl = "https:catfact.ninja", //todo refactor to build config
            converter = GsonConverterFactory.create()
        ).create(CatService::class.java)
    }

    override fun getCatBreeds(
        limit: Int
    ): Flow<List<CatBreedItem>> {
        return getCatService().getCatBreeds(limit).flowOn(coroutineDispatcher)
    }
}