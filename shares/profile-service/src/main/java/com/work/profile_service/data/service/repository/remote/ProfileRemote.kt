package com.work.profile_service.data.service.repository.remote

import com.work.network.base.ApiManager
import com.work.profile_service.data.model.response.ProfileResponse
import com.work.profile_service.data.service.api.ProfileService
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory

interface IProfileRemote {
    suspend fun getProfile(): Response<ProfileResponse>
}

class ProfileRemote(
    private val apiManager: ApiManager
): IProfileRemote {
    fun getProfileService(): ProfileService {
        return apiManager.init(
            baseUrl = "https://randomuser.me/", //todo refactor to build config
            converter = GsonConverterFactory.create()
        ).create(ProfileService::class.java)
    }

    override suspend fun getProfile(): Response<ProfileResponse> {
        return getProfileService().getProfile()
    }
}