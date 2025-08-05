package com.work.network.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class NetworkBoundResource<ResponseType> {

    fun asFlow(): Flow<ResponseType> = flow {
        val localDataFlow = loadFromDb()
        val initialLocalData = localDataFlow.firstOrNull() // Get current value to decide shouldFetch

        if (shouldFetch(initialLocalData)) {

            try {
                val apiResponse = createCall()
                if (apiResponse.isSuccessful && apiResponse.body() != null) {
                    saveCallResult(apiResponse.body()!!)
                    emit(apiResponse.body()!!)
                } else {
                    throw HttpException(apiResponse)
                }
            } catch (e: HttpException) {
                throw e
            } catch (e: IOException) {
                throw e
            } catch (e: Exception) {
                throw e
            }
        } else {
            emitAll(localDataFlow.map { it })
        }
    }.flowOn(Dispatchers.IO)

    protected abstract suspend fun saveCallResult(item: ResponseType)

    protected suspend fun shouldFetch(data: ResponseType?): Boolean = true

    protected abstract suspend fun loadFromDb(): Flow<ResponseType>

    protected abstract suspend fun createCall(): Response<ResponseType>
}