package com.work.profile_service.data.service.repository

import com.work.network.repository.NetworkBoundResource
import com.work.profile_service.data.model.response.ProfileResponse
import com.work.profile_service.data.service.repository.remote.IProfileRemote
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface IProfileRepository {
    suspend fun getProfile(): Flow<ProfileResponse>
}

class ProfileRepository(
    private val profileRemote: IProfileRemote
): IProfileRepository {

    override suspend fun getProfile(): Flow<ProfileResponse> {
        return object: NetworkBoundResource<ProfileResponse>() {
            override suspend fun createCall(): Response<ProfileResponse> {
                return profileRemote.getProfile()
            }
        }.asFlow()
    }
}