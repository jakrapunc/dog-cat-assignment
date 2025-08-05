package com.work.dog_service.domain

import com.work.dog_service.data.model.DogData
import com.work.dog_service.data.service.repository.IDogRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

class GetSequenceDogListUseCase(
    private val dogRepository: IDogRepository,
    private val coroutineDispatcher: CoroutineDispatcher,
    private val convertTimeStampUseCase: ConvertTimeStampUseCase
) {
    suspend fun invoke(size: Int): Flow<List<DogData?>> {
        val dogDataList = withContext(coroutineDispatcher) {
            val result = mutableListOf<DogData?>()
            repeat(size) {
                try {
                    val dog = dogRepository.getRandomDog().first()
                    result.add(
                        DogData(
                            dogResponse = dog,
                            timeStamp = convertTimeStampUseCase.invoke(System.currentTimeMillis())
                        )
                    )
                } catch (e: Exception) {
                    null
                }
            }
            result
        }
        return flowOf(dogDataList)
    }
}