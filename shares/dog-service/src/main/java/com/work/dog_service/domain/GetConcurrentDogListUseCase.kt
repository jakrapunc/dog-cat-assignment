package com.work.dog_service.domain

import com.work.dog_service.data.model.DogData
import com.work.dog_service.data.service.repository.IDogRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

class GetConcurrentDogListUseCase(
    private val dogRepository: IDogRepository,
    private val convertTimeStampUseCase: ConvertTimeStampUseCase,
    private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend fun invoke(
        size: Int,
    ): Flow<List<DogData?>> {
        val timeStamp = mutableListOf<Long>()
        val dogDataList =  withContext(coroutineDispatcher) {
            val deferredDogs = List(size) {
                async {
                    try {
                        timeStamp.add(System.currentTimeMillis())
                        dogRepository.getRandomDog().first()
                    } catch (e: Exception) {
                        null
                    }
                }
            }
            deferredDogs.awaitAll()
        }.zip(timeStamp) { dog, time ->
            DogData(
                dogResponse = dog,
                timeStamp = convertTimeStampUseCase.invoke(time)
            )
        }

        return flowOf(dogDataList)
    }
}