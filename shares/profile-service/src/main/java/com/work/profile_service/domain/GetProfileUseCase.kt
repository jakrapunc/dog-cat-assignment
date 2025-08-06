package com.work.profile_service.domain

import com.work.profile_service.data.model.ProfileData
import com.work.profile_service.data.service.repository.IProfileRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GetProfileUseCase(
    private val profileRepository: IProfileRepository,
    private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend fun invoke(): Flow<ProfileData> = withContext(coroutineDispatcher) {
        profileRepository.getProfile().map {
            if (it.results.isEmpty()) {
                throw Exception("Profile is empty")
            }

            it.results[0]
        }
    }
}